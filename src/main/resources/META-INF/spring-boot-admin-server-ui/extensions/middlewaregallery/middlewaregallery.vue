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

  <div style="margin: 5%;">
    <div class="row">
      <div class="col-sm-6">
        <div class="card">
          <div class="card-body">
            <h5 class="card-title">Spring Cloud Gateway</h5>
            <p class="card-text">Spring Cloud Gateway is an API Gateway / Backend For the FrontEnd (BFF) framework. It is based on Spring 5, Spring Boot 2, and Project Reactor / Webflux and works on a non-blocking API model.</p>
            <a href="deploy/spring-cloud-gateway" class="btn btn-primary" @click="showapi1" v-if="show1">Deploy</a>
            <button class="btn btn-primary" v-if="!show1" disabled>
              <span class="spinner-border spinner-border-sm"></span>
              Loading...
            </button>
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
    }
  },
  methods: {
    showapi1() {
      this.show1 = !this.show1;
    },
    stringify: JSON.stringify,
  },
};
</script>

<style>
@import "https://cdn.jsdelivr.net/npm/bootstrap@4.0.0/dist/css/bootstrap.min.css"
</style>
