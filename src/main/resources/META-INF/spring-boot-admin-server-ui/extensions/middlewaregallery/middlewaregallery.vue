<!--
  - Copyright 2014-2018 the original author or authors.
  -
  - Licensed under the Apache License, Version 2.0 (the "License");
  - you may not use this file except in compliance with the License.
  - You may obtain a copy of the License at
  -
  -     http://www.apache.org/licenses/LICENSE-2.0
  -
  - Unless required by applicable law or agreed to in writing, software
  - distributed under the License is distributed on an "AS IS" BASIS,
  - WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  - See the License for the specific language governing permissions and
  - limitations under the License.
  -->

<template>

  <div style="margin: 5%;" v-if="show1&&!show2">
    <div class="row">
      <div class="col-sm-6">
        <div class="card">
          <div class="card-body">
            <h5 class="card-title">Spring Cloud Gateway</h5>
            <p class="card-text">Spring Cloud Gateway is an API Gateway / Backend For the FrontEnd (BFF) framework. It is based on Spring 5, Spring Boot 2, and Project Reactor / Webflux and works on a non-blocking API model.</p>
            <button class="btn btn-primary" @click="showapi1">Deploy</button>
          </div>
        </div>
      </div>
      <div class="col-sm-6">
        <div class="card">
          <div class="card-body">
            <h5 class="card-title">Nacos</h5>
            <p class="card-text">Nacos is an easy-to-use dynamic service discovery, configuration and service management platform for building cloud native applications.</p>
            <a href="#" class="btn btn-primary">Deploy</a>
          </div>
        </div>
      </div>
    </div>

    <br/>

    <div class="row">
      <div class="col-sm-6">
        <div class="card">
          <div class="card-body">
            <h5 class="card-title">Apollo Config Server</h5>
            <p class="card-text">Apollo is a reliable configuration management system.</p>
            <a href="#" class="btn btn-primary">Deploy</a>
          </div>
        </div>
      </div>
      <div class="col-sm-6">
        <div class="card">
          <div class="card-body">
            <h5 class="card-title">Sentinel</h5>
            <p class="card-text">Sentinel is a powerful flow control component enabling reliability, resilience and monitoring for microservices.</p>
            <a href="#" class="btn btn-primary">Deploy</a>
          </div>
        </div>
      </div>
    </div>
  </div>

  <div class="log-in" v-if="show2&&!show1" style="width: 350px;height: 200px;background-color: white;padding: 5px;border: 1px solid black;margin: 10% auto">
    <div class="dowebok">
      <img src="https://aadcdn.msftauth.net/shared/1.0/content/images/microsoft_logo_ee5c8d9fb6248c938fd0dc19370e90bd.svg">
      <div class="form-item">
        <input id="username" type="text" autocomplete="off" placeholder="Email or phone number">
      </div>
      <div class="form-item">
        <input id="password" type="password" autocomplete="off" placeholder="Password">
      </div>
      <div class="form-item">
        <button id="submit" @click="showapi2" class="btn btn-primary">Log In</button>
      </div>
      <div class="reg-bar">
        <a class="btn btn-info" href="#" target="_blank" >Create new account</a>
        <a class="btn btn-warning" href="#" target="_blank">Forgot password?</a>
      </div>
    </div>
  </div>
  <div class="selected-asa" v-if="!show2&&!show1&&!showButton" style="margin: 5% auto">
    <div class="form-group">
      <label for="exampleFormControlSelect1">Select Azure Spring Apps Instance Name</label>
      <select class="form-control" id="exampleFormControlSelect1">
        <option disabled>Please select the Asa you want to use</option>
        <option>asa1</option>
        <option>asa-test</option>
        <option>asa-test1</option>
      </select>
    </div>
<!--    <button class="btn btn-primary" @click="showwapi3" v-if="!show2&&!show1&&!showButton">Deploy</button>-->
    <a href="deploy/spring-cloud-gateway" class="btn btn-primary" @click="showwapi3" v-if="!show2&&!show1&&!showButton">Deploy</a>
  </div>
  <div id="loading..." v-if="!show2&&!show1&&showButton">
    <div class="d-flex align-items-center">
      <strong>creating, please wait...</strong>
      <div class="spinner-border ml-auto" role="status" aria-hidden="true"></div>
    </div>
  </div>
</template>

<script>
/* global SBA */
export default {
  setup() {
    // const { applications } = SBA.useApplicationStore(); //<1>
    const applications = [{"name":"aa"}, {"name":"bb"}];
    return {
      applications,
    };
  },
  data() {
    return {
      show1: true,
      show2: false,
      showButton: false,
    }
  },
  methods: {
    showapi1() {
      this.show1 = false;
      this.show2 = true;
    },
    showapi2() {
      this.show2 = !this.show2;
    },
    showwapi3() {
      this.showButton = true;
    },
    stringify: JSON.stringify,
  },
};
</script>

<style>
@import "https://cdn.jsdelivr.net/npm/bootstrap@4.0.0/dist/css/bootstrap.min.css"
</style>
