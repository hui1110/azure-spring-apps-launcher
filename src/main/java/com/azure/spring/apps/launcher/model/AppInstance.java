package com.azure.spring.apps.launcher.model;

public class AppInstance {

        private String id;

        private String name;


        public AppInstance() {
        }

        public AppInstance(String id, String name) {
            this.id = id;
            this.name = name;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

