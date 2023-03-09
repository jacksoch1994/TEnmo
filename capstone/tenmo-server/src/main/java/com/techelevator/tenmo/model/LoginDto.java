package com.techelevator.tenmo.model;
/*
    The acronym DTO is being used for "data transfer object". It means that this type of class is specifically
    created to transfer data between the client and the server. For example, LoginDto represents the data a client
    must pass to the server for a login endpoint, and LoginResponseDto represents the object that's returned from the server
    to the client from a login endpoint.
 */
public class LoginDto {

   /*
    ########################################   Attributes   ##########################################
    */

   private String username;
   private String password;

   /*
    ######################################## Getter Methods ##########################################
    */

   public String getUsername() {
      return username;
   }

   public String getPassword() {
      return password;
   }

   /*
    ######################################## Setter Methods ##########################################
    */

   public void setUsername(String username) {
      this.username = username;
   }

   public void setPassword(String password) {
      this.password = password;
   }

   /*
    ######################################  Override Methods  ########################################
    */

   @Override
   public String toString() {
      return "LoginDto{" +
              "username='" + username + '\'' +
              ", password='" + password + '\'' +
              '}';
   }
}
