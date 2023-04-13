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
  <!--Card Page-->
  <div v-if="showCard">

    <div class="alert alert-primary" role="alert">
      <div class="text-center">
        <div class="h1">Launch From the Browser</div>
        <span class="badge badge-pill badge-primary">-- Launch popular middleware loved by spring community from browser with one click</span>
      </div>
    </div>
    <div class="row">
      <div class="col-sm-6">
        <div class="card">
          <div class="card-body">
            <h5 class="card-title">Spring Cloud Gateway</h5>
            <p class="card-text">Spring Cloud Gateway is an API Gateway / Backend For the FrontEnd (BFF) framework. It is based on Spring 5, Spring Boot 2, and Project Reactor / Webflux and works on a non-blocking API model.</p>
            <button class="btn btn-primary" @click="showApiLogin">Deploy</button>
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

    <div class="alert alert-primary" role="alert">
      <p> </p>
    </div>
  </div>

  <!--Select Page-->
  <div v-if="!showCard&&!showLoading">
    <div class="alert alert-primary" role="alert">
      <div class="text-center">
        <div class="h1">Last Step before Deploy</div>
      </div>
    </div>
    <div class="selected-asa" style="width: 400px;height: 280px;background-color:#C0C4CC;padding: 5px;border: 1px solid black;margin: 0px auto">
      <div class="form-group">
        <label for="selectSpringBootVersion">
          <div class="h5">Choose Spring Cloud Gateway Version</div>
        </label>
        <select class="form-control" v-model="gatewayVersion" id="selectSpringCloudGatewayVersion">
          <option>4.0.4</option>
          <option>3.1.6</option>
        </select>
      </div>

      <div class="form-group">
        <label for="selectSubId">
          <div class="h5">Choose Subscription Id you want to use</div>
        </label>
        <select class="form-control" v-model="subId" id="selectSubId" @change="changeASAInstance(subId)">
          <option v-for="item in SubList" :key="item.name" :value="item.id">
            {{ item.name }}
          </option>
        </select>
      </div>

      <div class="form-group">
        <label for="SelectAsaInstance">
          <div class="h5">Select Azure Spring Apps Instance Name</div>
        </label>
        <select class="form-control" v-model="ASAName" id="SelectAsaInstance">
          <option disabled>Please select the Asa you want to use</option>
          <option v-for="item in ASAList" :key="item.name" :value="item.name">
            {{ item.name }}
          </option>
        </select>
      </div>
      <button class="btn btn-primary" @click="showApiLoading" v-if="!showCard&&!showLoading">Deploy</button>
    </div>
  </div>

  <!--Loading Page-->
  <div id="loading..." class="col-sm-8" v-if="!showCard&&showLoading">

    <div id="stage.prepare" class="card" >
      <div class="h5">Prepare Jar</div>
      <div class="card-body">
        <div class="progress">
          <div class="progress-bar" role="progressbar" :style="{ width: processPercent4Prepare + '%'}" aria-valuemin="0" aria-valuemax="100">{{
              processPercent4Prepare + '%'
            }}</div>
        </div>
        <div v-if="processPercent4Prepare < 100" >Preparing the Jar file...</div>
        <div v-else>succeed</div>
      </div>
    </div>

    <div id="stage.create" class="card">
      <div class="h5">Create ASA app</div>
      <div class="card-body">
        <div class="progress">
          <div class="progress-bar" role="progressbar" :style="{ width: processPercent4Create + '%'}" aria-valuemin="0" aria-valuemax="100">{{
              processPercent4Create + '%'
            }}</div>
        </div>
        <div v-if="processPercent4Create < 100">Creating Spring Cloud Gateway app...</div>
        <div v-else>succeed</div>
      </div>
    </div>

    <div id="stage.deploy" class="card">
      <div class="h5">Deploy to ASA app</div>
      <div class="card-body">
        <div class="progress">
          <div class="progress-bar" role="progressbar" :style="{ width: processPercent4Deploy + '%'}" aria-valuemin="0" aria-valuemax="100">{{
              processPercent4Deploy + '%'
            }}</div>
        </div>
        <div v-if="processPercent4Deploy < 100">Deploy Jar file to Spring Cloud Gateway app...</div>
        <div v-else>succeed</div>
      </div>
    </div>

    <div id="redirect-buttons" v-show="showRedirectButton">
      <a class="btn btn-primary" :href="'https://ms.portal.azure.com/#@microsoft.onmicrosoft.com/resource/subscriptions/'+this.subId+'/resourceGroups/'+this.rgGroup+'/providers/Microsoft.AppPlatform/Spring/'+this.ASAName+'/application_dashboard'" role="button">Redirect to Azure ASA Portal</a>
      <a class="btn btn-primary" :href="'https://'+this.ASAName+'-spring-cloud-gateway.azuremicroservices.io/'" role="button">Redirect to ASA App endpoint</a>
    </div>

  </div>
</template>

<script>
import 'jquery/dist/jquery.min'
import 'bootstrap/dist/js/bootstrap.bundle'
import axios from 'axios'

Vue.proto
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
      showCard: true,
      showLoading: false,
      showRedirectButton: false,
      processPercent4Prepare: 0,
      processPercent4Create: 0,
      processPercent4Deploy: 0,
      timer1: null,
      timer2: null,
      timer3: null,
      gatewayVersion: '',
      subId: '',
      rgGroup: '',
      ASAName: '',
      SubList: [],
      ASAList: [],
    }
  },
  methods: {
    showApiLogin() {
      this.querySubId();
      this.showCard = false;
    },
    showApiLoading() {
      this.showLoading = true;
      console.log("creating app...")
      // this.queryResourceGroup();
      axios.get('deploy/spring-cloud-gateway/'+ this.gatewayVersion+'/'+this.queryResourceGroup()+'/'+this.ASAName+'/'+this.subId)
          .then(res => {
            if (res.data == true) {
              this.processPercent4Deploy = 100;
              this.showRedirectButton = true;
              console.log("created successfully")
            }
          })
    },
    queryProgress() {
      axios.get('queryProcess/'+this.rgGroup+'/'+this.ASAName+'/'+this.subId)
          .then(res => {
            console.log(res.data);
            if (res.data === 100) {
              this.processPercent4Create = res.data;
            }
          })
          .catch(err => {
            console.log(err);
          })
    },
    querySubId() {
      axios.get('/subscriptionList')
          .then(res => {
            console.log(res.data);
            this.SubList = res.data
          })
          .catch(err => {
            console.log(err);
          })
    },
    changeASAInstance(subId) {
      axios.get("/instanceList/"+subId)
          .then(res => {
            console.log(res.data);
            this.ASAList = res.data;
          })
          .catch(err => {
            console.log(err);
          })
    },
    queryResourceGroup() {
      for (let i = 0; i < this.ASAList.length; i++) {
        if ( this.ASAList[i].name === this.ASAName ) {
          let rg = this.ASAList[i].id.split("/")[4];
          console.log(rg);
          this.rgGroup = rg;
          return rg;
        }
      }
    },
    stringify: JSON.stringify,
  },
  mounted() {
    this.timer1 = setInterval(() => {
      if (!this.showCard&&this.showLoading) {
        if (this.processPercent4Prepare < 100) {
          this.processPercent4Prepare ++;
          console.log(this.processPercent4Prepare)
        }
      }
    },50);
    this.timer2 = setInterval(() => {
      if (!this.showCard&&this.showLoading&&this.processPercent4Prepare===100) {
        if (this.processPercent4Create < 99) {
          this.queryProgress();
          this.processPercent4Create ++ ;
        }
      }
    },200);
    this.timer3 = setInterval(() => {
      if (!this.showCard&&this.showLoading&&this.processPercent4Create===100) {
        if (this.processPercent4Deploy < 99) {
          this.processPercent4Deploy ++ ;
        }
      }
    },5000);
  },
  beforeDestroy() {
    clearInterval(this.timer1);
    clearInterval(this.timer2);
    clearInterval(this.timer3);
  },

};
</script>

<!--<style src="bootstrap/dist/css/bootstrap.min.css" scoped>-->
<style>
@import "https://cdn.jsdelivr.net/npm/bootstrap@4.6.2/dist/css/bootstrap.min.css";
</style>
