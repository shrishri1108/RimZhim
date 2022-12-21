package com.rimzhim.ModelClasses;
 public class LoginResponse {
     private String result;
     private String token;
     private String message;
     private User user;

     public String getResult() {
         return result;
     }

     public void setResult(String result) {
         this.result = result;
     }

     public String getToken() {
         return token;
     }

     public void setToken(String token) {
         this.token = token;
     }

     public String getMessage() {
         return message;
     }

     public void setMessage(String message) {
         this.message = message;
     }

     public User getUser() {
         return user;
     }

     public void setUser(User user) {
         this.user = user;
     }

     public static class User {
         private int id;
         private int roleId;
         private String name;
         private String email;
         private String phone;
         private String stateId;
         private String cityId;
         private String user_name;
         private String referralCode;
         private String dob;
         private String longitude;
         private String gender;
         private String wallet;
         private String referUserId;
         private String latitude;
         private int status;
         private String bio;
         private String winning;
         private String background_image;
         private String image;
         private int isDelete;
         private String address;
         private String createdAt;
         private String updatedAt;
         private String timeOfBirth;
         private String socialId;
         private String socialType;

         public User(int id, int roleId, String name, String email, String phone, String stateId, String cityId, String user_name, String referralCode, String dob, String longitude, String gender, String wallet, String referUserId, String latitude, int status, String bio, String winning, String background_image, String image, int isDelete, String address, String createdAt, String updatedAt, String timeOfBirth, String socialId, String socialType) {
             this.id = id;
             this.roleId = roleId;
             this.name = name;
             this.email = email;
             this.phone = phone;
             this.stateId = stateId;
             this.cityId = cityId;
             this.user_name = user_name;
             this.referralCode = referralCode;
             this.dob = dob;
             this.longitude = longitude;
             this.gender = gender;
             this.wallet = wallet;
             this.referUserId = referUserId;
             this.latitude = latitude;
             this.status = status;
             this.bio = bio;
             this.winning = winning;
             this.background_image = background_image;
             this.image = image;
             this.isDelete = isDelete;
             this.address = address;
             this.createdAt = createdAt;
             this.updatedAt = updatedAt;
             this.timeOfBirth = timeOfBirth;
             this.socialId = socialId;
             this.socialType = socialType;
         }

         public int getId() {
             return id;
         }

         public void setId(int id) {
             this.id = id;
         }

         public int getRoleId() {
             return roleId;
         }

         public void setRoleId(int roleId) {
             this.roleId = roleId;
         }

         public String getName() {
             return name;
         }

         public void setName(String name) {
             this.name = name;
         }

         public String getEmail() {
             return email;
         }

         public void setEmail(String email) {
             this.email = email;
         }

         public String getPhone() {
             return phone;
         }

         public void setPhone(String phone) {
             this.phone = phone;
         }

         public String getStateId() {
             return stateId;
         }

         public void setStateId(String stateId) {
             this.stateId = stateId;
         }

         public String getCityId() {
             return cityId;
         }

         public void setCityId(String cityId) {
             this.cityId = cityId;
         }

         public String getUser_name() {
             return user_name;
         }

         public void setUser_name(String user_name) {
             this.user_name = user_name;
         }

         public String getReferralCode() {
             return referralCode;
         }

         public void setReferralCode(String referralCode) {
             this.referralCode = referralCode;
         }

         public String getDob() {
             return dob;
         }

         public void setDob(String dob) {
             this.dob = dob;
         }

         public String getLongitude() {
             return longitude;
         }

         public void setLongitude(String longitude) {
             this.longitude = longitude;
         }

         public String getGender() {
             return gender;
         }

         public void setGender(String gender) {
             this.gender = gender;
         }

         public String getWallet() {
             return wallet;
         }

         public void setWallet(String wallet) {
             this.wallet = wallet;
         }

         public String getReferUserId() {
             return referUserId;
         }

         public void setReferUserId(String referUserId) {
             this.referUserId = referUserId;
         }

         public String getLatitude() {
             return latitude;
         }

         public void setLatitude(String latitude) {
             this.latitude = latitude;
         }

         public int getStatus() {
             return status;
         }

         public void setStatus(int status) {
             this.status = status;
         }

         public String getBio() {
             return bio;
         }

         public void setBio(String bio) {
             this.bio = bio;
         }

         public String getWinning() {
             return winning;
         }

         public void setWinning(String winning) {
             this.winning = winning;
         }

         public String getBackground_image() {
             return background_image;
         }

         public void setBackground_image(String background_image) {
             this.background_image = background_image;
         }

         public String getImage() {
             return image;
         }

         public void setImage(String image) {
             this.image = image;
         }

         public int getIsDelete() {
             return isDelete;
         }

         public void setIsDelete(int isDelete) {
             this.isDelete = isDelete;
         }

         public String getAddress() {
             return address;
         }

         public void setAddress(String address) {
             this.address = address;
         }

         public String getCreatedAt() {
             return createdAt;
         }

         public void setCreatedAt(String createdAt) {
             this.createdAt = createdAt;
         }

         public String getUpdatedAt() {
             return updatedAt;
         }

         public void setUpdatedAt(String updatedAt) {
             this.updatedAt = updatedAt;
         }

         public String getTimeOfBirth() {
             return timeOfBirth;
         }

         public void setTimeOfBirth(String timeOfBirth) {
             this.timeOfBirth = timeOfBirth;
         }

         public String getSocialId() {
             return socialId;
         }

         public void setSocialId(String socialId) {
             this.socialId = socialId;
         }

         public String getSocialType() {
             return socialType;
         }

         public void setSocialType(String socialType) {
             this.socialType = socialType;
         }
     }
 }











