package com.example.thevisualbookapplication;

class PictureFileModel {
   public String name;
   public String uri;

   public String getName() {
      return name;
   }

   public String getUri() {
      return uri;
   }

   public PictureFileModel(String name, String uri) {
      this.name = name;
      this.uri = uri;
   }

   public PictureFileModel(){

   }

}
