package ticket.booking.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import ticket.booking.entities.Ticket;
import ticket.booking.entities.User;
import ticket.booking.util.UserServiceUtil;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class UserBookingService {

    public User user;
    public List<User> userList;
    private ObjectMapper objectMapper = new ObjectMapper();
    private static final String USER_FILE_PATH = "../localDb/Users.json";

    UserBookingService(User user) throws IOException {
        this.user = user;
        //load users from users.json
        File users = new File(USER_FILE_PATH);
        userList = objectMapper.readValue(users, new TypeReference<List<User>>(){});
    }

    public Boolean loginUser(){
        Optional<User> foundUser = userList.stream().filter(user1 -> {
            return user1.getName().equals(user.getName()) && UserServiceUtil.checkPassword(user.getPassword(), user1.getHashedPassword());
        }).findFirst();
        return foundUser.isPresent();
    }

    public Boolean signUp(User user1){
        try{
            userList.add(user1);
            saveUserListToFile();
            return Boolean.TRUE;
        }catch (IOException ex){
            return Boolean.FALSE;
        }
    }

    private void saveUserListToFile() throws IOException {
        File usersFile = new File(USER_FILE_PATH);
        objectMapper.writeValue(usersFile, userList);
    }

    public void fetchBookings(){
        user.printTickets();
    }

    public Boolean cancelBooking(String ticketId){
//        if(ticketId == null || ticketId.isEmpty()){
//            return Boolean.FALSE;
//        }
//        Optional<Ticket> ticketToCancel = user.getTicketsBooked().stream().filter(ticket -> {
//            return ticket.getTicketId().equals(ticketId);
//        }).findFirst();
//        if(ticketToCancel.isPresent()) {
//            List<Ticket> tickets = user.getTicketsBooked().stream().filter(ticket -> ticket.getTicketId() != (ticketId)).collect(Collectors.toList());
//            user.setTicketsBooked(tickets);
//            return Boolean.TRUE;
//        }
//        return Boolean.FALSE;
            if (ticketId == null || ticketId.isEmpty()) {
                return Boolean.FALSE;
            }

            boolean removed = user.getTicketsBooked().removeIf(ticket -> ticket.getTicketId().equals(ticketId));

            return removed ? Boolean.TRUE : Boolean.FALSE;
    }

}
