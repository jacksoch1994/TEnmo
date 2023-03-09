package com.techelevator.tenmo.model;

import java.util.Objects;

public class Authority {

   /*
    ########################################   Attributes   ##########################################
     */

   private String name;

   /*
    ######################################## Getter Methods ##########################################
     */

   public String getName() {
      return name;
   }

   /*
    ######################################## Setter Methods ##########################################
     */

   public void setName(String name) {
      this.name = name;
   }

   /*
    ########################################   Constructor   ##########################################
     */

   public Authority(String name) {
      this.name = name;
   }

   /*
    ######################################  Override Methods  ########################################
     */

   @Override
   public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      Authority authority = (Authority) o;
      return name.equals(authority.name);
   }

   @Override
   public int hashCode() {
      return Objects.hash(name);
   }

   @Override
   public String toString() {
      return "Authority{" +
         "name=" + name +
         '}';
   }
}
