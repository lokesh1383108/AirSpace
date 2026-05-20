package com.example.airspace.AirSpace.service;


import com.example.airspace.AirSpace.DTOs.BookingRequestDTO;
import com.example.airspace.AirSpace.DTOs.BookingResponseDTO;
import com.example.airspace.AirSpace.models.BookingRequest;
import com.example.airspace.AirSpace.models.BookingRequest.BookingStatus;
import com.example.airspace.AirSpace.models.Property;
import com.example.airspace.AirSpace.models.user.Customer;
import com.example.airspace.AirSpace.models.user.User;
import com.example.airspace.AirSpace.repositories.*;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class BookingService {

    @Autowired
    private BookingRequestRepository bookingRequestRepository;

    @Autowired
    private AuthService authService;

    @Autowired
    private PropertyRepository propertyRepository;

    @Autowired
    private HostRepository hostRepository;

    @Autowired
    private UserRepository userRepository;


    @Autowired
    private CustomerRepository customerRepository;

    /**
     * Entry point for creating a booking request.
     */
    public BookingResponseDTO createBookingRequest(BookingRequestDTO bookingRequest, Principal principal) {
        // Validate input and create a booking
        BookingRequest booking = buildBookingRequest(bookingRequest, principal);
       // Validating that Booking is already exists or not
       if(isExistingBooking(booking)){
            throw new RuntimeException("Booking already exists.");
       }

        //payment gateway (Optional for now)


        // Save booking to the database
        return mapToResponseDTO(saveBooking(booking));

        // notificationToHostViaFcm()   // for both android and web app they can see in notification bar of  phone

        // Additional actions (e.g., notify host) can be added here
        // notifyHost(booking);

        
    }

    // helper to convert model guest composition -> DTO guest composition
    private BookingRequestDTO.GuestComposition mapGuestsToDto(BookingRequest.GuestComposition guests) {
        if (guests == null) return null;
        return BookingRequestDTO.GuestComposition.builder()
                .maleGuests(guests.getMaleGuests())
                .femaleGuests(guests.getFemaleGuests())
                .children(guests.getChildren())
                .transGuests(guests.getTransgenderGuests()) // map naming differences
                .build();
    }


    /**
     * Map persisted BookingRequest to a response DTO for API clients.
     */
    private BookingResponseDTO mapToResponseDTO(BookingRequest booking) {
        
            if (booking == null) {
            return null;
        }

        return BookingResponseDTO.builder()
            .BookingId(booking.getId()) // Already String
            .propertyId(convertObjectIdToString(booking.getPropertyId()))
            .userId(convertObjectIdToString(booking.getUserId()))
            .checkInTime(booking.getCheckInTime())
            .checkOutTime(booking.getCheckOutTime())
            .guests(mapGuestsToDto(booking.getGuests()))
            .status(mapStatus(booking.getStatus()))
            .createdAt(booking.getCreatedAt())
            .updatedAt(booking.getUpdatedAt())
            .build();
    }
    // Helper method for ObjectId to String conversion
    private String convertObjectIdToString(ObjectId objectId) {
        return objectId != null ? objectId.toHexString() : null;
    }

    private String mapStatus(BookingStatus status) {
        return status != null ? status.name() : "PENDING";
    }

    /**
     * Checking in database is booking for specific date and property already exists or not
     */
    private boolean isExistingBooking(BookingRequest booking) {
        return bookingRequestRepository.existsByPropertyIdAndCheckInTimeLessThanEqualAndCheckOutTimeGreaterThanEqual(
                booking.getPropertyId(),
                booking.getCheckInTime(),
                booking.getCheckOutTime()
        );
    }


    /**
     * Builds a BookingRequest object by validating and mapping input data.
     */
    private synchronized BookingRequest buildBookingRequest(BookingRequestDTO bookingRequest, Principal principal) {
        // Validate property and fetch its details
        Property property = validateAndGetProperty(bookingRequest.getPropertyId());

        // Validate booking dates
        validateBookingDates(bookingRequest.getCheckInTime(), bookingRequest.getCheckOutTime());

        // Get the authenticated user
        User user = authService.findByEmail(principal.getName())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        // Continue the Booking if Number of beds if greater than Zero
        List<Property.Rooms> rooms=property.getRooms();
        rooms.forEach(rooms1 -> {
            System.out.println("No of beds "+rooms1.getVacantBeds());
        });

        // when beds > 0
        // Map data from DTO to model
        return mapToBookingRequest(bookingRequest, property, user);
    }

    /**
     * Validates and fetches the property by its ID.
     */
    private Property validateAndGetProperty(String propertyId) {

        if (!ObjectId.isValid(propertyId)) {
            throw new IllegalArgumentException("Invalid Property ID format");
        }
        ObjectId propertyObjectId = new ObjectId(propertyId);
        return propertyRepository.findById(propertyObjectId)
                .orElseThrow(() -> new IllegalArgumentException("Property not found"));
    }

    /**
     * Validates check-in and check-out dates.
     */
    private void validateBookingDates(LocalDateTime checkInTime, LocalDateTime checkOutTime) {
        if (checkInTime.isAfter(checkOutTime)) {
            throw new IllegalArgumentException("Check-in time must be before check-out time");
        }
        if (checkInTime.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Check-in time cannot be in the past");
        }
    }

    /**
     * Maps BookingRequestDTO to BookingRequest model.
     */
    private BookingRequest mapToBookingRequest(BookingRequestDTO bookingRequest, Property property, User user) {
        BookingRequest booking = new BookingRequest();
        booking.setUserId(user.getId());
        booking.setPropertyId(property.getId());
        booking.setCheckInTime(bookingRequest.getCheckInTime());
        booking.setCheckOutTime(bookingRequest.getCheckOutTime());
        booking.setGuests(mapToModel(bookingRequest));
//        booking.setHostId(property.getHostId()); // Optional if needed
//        booking.setStatus(BookingRequest.BookingStatus.PENDING); // Default status
        return booking;
    }

    /**
     * Persists the BookingRequest to the database.
     */
    private BookingRequest saveBooking(BookingRequest booking) {
        return bookingRequestRepository.save(booking);
    }

    private BookingRequest.GuestComposition mapToModel(BookingRequestDTO bookingRequest){
       return BookingRequest.GuestComposition.builder()
                .maleGuests(bookingRequest.getGuests().getMaleGuests())
                .femaleGuests(bookingRequest.getGuests().getFemaleGuests())
                .children(bookingRequest.getGuests().getChildren())
                .transgenderGuests(bookingRequest.getGuests().getTransGuests())
                .build();
    }

//    // Method to notify the host about the booking request
//    private void notifyHost(BookingRequest bookingRequest) {
//        Optional<Host> hostOpt = hostRepository.findById();
//        if (hostOpt.isPresent()) {
//            Host host = hostOpt.get();
//            Optional<User> user=userRepository.findById(host.getUserId());
//            if(user.isPresent()){
//            String hostToken = user.get().getFcmToken();
//            new NotificationService(new RestTemplate()).sendBookingNotification(hostToken, bookingRequest);
//            }
//        }
//    }

    // Method to accept booking
    public void acceptBooking(ObjectId bookingId) {
        try{
        Optional<BookingRequest> requestOpt = bookingRequestRepository.findById(bookingId);
        if (requestOpt.isPresent()) {
            BookingRequest request = requestOpt.get();
            BookingStatus status= request.getStatus();
            if(status.equals(BookingRequest.BookingStatus.CONFIRMED) || status.equals(BookingRequest.BookingStatus.CANCELLED)){
                throw new RuntimeException("Cannot proceed the action. Booking status already " + status);
            }
            request.setStatus(BookingRequest.BookingStatus.CONFIRMED);
            bookingRequestRepository.save(request);

            // Notify customer with host contact details
            Optional<Customer> customerOpt = customerRepository.findById(request.getUserId());
            if (customerOpt.isPresent()) {
                Customer customer = customerOpt.get();
                // Send contact details to customer
            }
        }
    }catch(Exception e){

    }
    }

    // Method to reject booking
    public void rejectBookingByHost(ObjectId bookingId, String reason) {
        BookingRequest booking = bookingRequestRepository.findById(bookingId)
        .orElseThrow(()-> new RuntimeException("Booking not found: "+ bookingId));

        // 1. If already rejected, show friendly message
    if (booking.getStatus() == BookingStatus.REJECTED) {
        throw new RuntimeException(
                "This booking has already been rejected by the host. Please try a different booking."
        );
    }
    
        // 2. Validate current status
    if (!booking.getStatus().equals(BookingStatus.PENDING)) {
        throw new RuntimeException(
                "Booking cannot be rejected. Current status: " + booking.getStatus()
        );
    }

    // 2. Update status
    booking.setStatus(BookingStatus.REJECTED);
    bookingRequestRepository.save(booking);

            // Notify customer about the rejection
            Optional<Customer> customerOpt = customerRepository.findById(booking.getUserId());
            if (customerOpt.isPresent()) {
                Customer customer = customerOpt.get();
                // Send rejection message to customer
                // sendEmail(customer.getEmail(), "Your booking request was rejected.");
            }

            
        }
        
    } 

