package com.example.thevisualbookapplication;

class VideoFileModel {
   public String name;
   public String uri;

   public String getName() {
      return name;
   }

   public String getUri() {
      return uri;
   }

   public VideoFileModel(String name, String uri) {
      this.name = name;
      this.uri = uri;
   }

   public VideoFileModel(){

   }
}
