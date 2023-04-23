<template>
  <div class="selected-asa" style="width: 35%;background-color:#C0C4CC;padding: 5px;border: 1px solid black;margin: 10px auto">

    <div class="form-group">
      <label for="repo">
        <div class="h6">GitHub repository address:</div>
        <input type="text" class="form-control" v-model="url" disabled/>
      </label>
    </div>

    <div class="form-group">
      <label for="selectSubId">
        <div class="h6">Choose Subscription Id you want to use:</div>
      </label>
      <select class="form-control" v-model="subId" id="selectSubId" @change="changeASAInstance(subId)">
        <option v-for="item in SubList" :key="item.name" :value="item.id">
          {{ item.name }}
        </option>
      </select>
    </div>

    <div class="form-group">
      <label for="SelectAsaInstance">
        <div class="h6">Select Azure Spring Apps Instance Name:</div>
      </label>
      <select class="form-control" v-model="ASAName" id="SelectAsaInstance" placeholder="Please select the Asa you want to use" @change="changeAppInstance(ASAName)">
        <option v-for="item in ASAList" :key="item.name" :value="item.name">
          {{ item.name }}
        </option>
      </select>
      <span>(No Azure Spring Apps instance, click <a @click="showDialog = true" style="color: #0366d6; cursor: pointer;">here</a> to create one.)</span>
    </div>

    <div class="form-group">
      <label for="SelectAppInstance">
        <div class="h6">Select App Instance Name:</div>
      </label>
      <select class="form-control" v-model="AppName" placeholder="Please select the App you want to use" id="SelectAppInstance">
        <option v-for="item in AppList" :key="item.name" :value="item.name">
          {{ item.name }}
        </option>
      </select>
    </div>

    <div class="form-group">
      <label for="SelectJavaVersion">
        <div class="h6">Select Java version:</div>
      </label>
      <span><a>(Please select the same Java version as the application.)</a></span>
      <select class="form-control" v-model="JavaVersion" placeholder="Please select the Java version you want to use" id="SelectJavaVersion">
        <option v-for="item in JavaVersionList" :key="item" :value="item">
          {{ item }}
        </option>
      </select>
    </div>

    <div class="form-group">
    <label>
      <input class="mui-switch" id="action" type="checkbox"> Enable GitHub Action
    </label>
    </div>

    <div class="form-group">
      <button class="btn btn-secondary" @click="deployCode()">Deploy</button>
    </div>

  </div>


<!--Create App tab-->
  <div  class="modal-tab" v-if="showDialog">

    <div class="form-group">
      <label for="SelectResourceGroup">
        <div class="h5">Create Azure Spring App and deploy source code.</div>
      </label>
    </div>

    <div class="form-group">
      <label for="SelectResourceGroup">
        <div class="h6">Select Resource group</div>
      </label>
      <select class="form-control" v-model="resourceGroupName" id="SelectResourceGroup">
        <option v-for="item in resourceGroup" :key="item.name" :value="item.name">
          {{ item.name }}
        </option>
      </select>
    </div>

    <div class="form-group">
      <label for="SelectAppInstance">
        <div class="h6">Type Azure Spring Apps Instance Name</div>
      </label>
      <input type="text" class="form-control" id="instanceName"/>
    </div>

    <div class="form-group">
      <label for="TypeAppInstance">
        <div class="h6">Type App Instance Name</div>
      </label>
      <input type="text" class="form-control" id="appName"/>
    </div>

    <div class="form-group">
      <label for="SelectRegion">
        <div class="h6">Select Region</div>
      </label>
      <span><a>(Please select the same Java version as the application.)</a></span>
      <select class="form-control" v-model="RegionName" id="SelectRegion">
        <option v-for="item in regionList" :key="item.label" :value="item.name">
          {{ item.label }}
        </option>
      </select>
    </div>

    <div class="form-group">
      <label for="SelectJavaVersion">
        <div class="h6">Select Java version</div>
      </label>
      <select class="form-control" v-model="JavaVersion" placeholder="Please select the Java version you want to use" id="SelectJavaVersion">
        <option v-for="item in JavaVersionList" :key="item" :value="item">
          {{ item }}
        </option>
      </select>
    </div>

    <div class="form-group">
      <label>
        <input class="mui-switch" id="createAction" type="checkbox"> Enable GitHub Action
      </label>
    </div>

    <div class="modal-footer">
      <button type="button" class="btn btn-outline-primary" @click="showDialog = false">Close
      </button>
      <button type="button" class="btn btn-outline-dark" @click="createAndDeploy()">Creste and Deploy</button>
    </div>

  </div>

<!--  Loading page-->

  <div class="modal-tab" v-if="showProgress">
    <div class="form-group">
      <label>
        <div class="h6">Deploy code ...</div>
      </label>
    </div>

    <div class="form-group">
    <div class="progress-bar" role="progressbar" style="width: 100%;" aria-valuenow="25" aria-valuemin="0" aria-valuemax="100">{{num}} %</div>
    </div>

    <div class="modal-footer">
      <button disabled type="button" id="viewEndpoint" class="btn btn-outline-primary" @click="viewEndpoint()">View Endpoint</button>
      <button disabled type="button" id="viewOnPortal" class="btn btn-outline-dark"  @click="viewOnPortal()">View on Portal</button>
      <button class="btn btn-light" @click="showProgress = false">Close</button>
    </div>

  </div>

</template>

<style>
.modal-tab{
  width: 40%;
  background-color: #dddddd;
  position: absolute;
  top: 40%;
  left: 30%;
  transform: translate(0, -50%);
}

label{
  display:block;
  vertical-align: middle;
}
label, input, select{
  vertical-align: middle;
}
.mui-switch {
  width: 52px;
  height: 31px;
  position: relative;
  border: 1px solid #dfdfdf;
  background-color: #fdfdfd;
  box-shadow: #dfdfdf 0 0 0 0 inset;
  border-radius: 20px;
  background-clip: content-box;
  display: inline-block;
  -webkit-appearance: none;
  user-select: none;
  outline: none;
}


</style>
<script>
import 'jquery/dist/jquery.min'
import 'bootstrap/dist/js/bootstrap.bundle'
import axios from 'axios'

Vue.proto
export default {
  props: ['gitRepoUrl'],
  setup() {
    const applications = [{"name":"aa"}, {"name":"bb"}];
    return {
      applications,
    };
  },
  data() {
    return {
      url: sessionStorage.getItem("githubRepoUrl"),
      RegionName: '',
      resourceGroupName: "",
      showDialog: false,
      showProgress: false,
      subId: '',
      ASAName: "",
      JavaVersion: "",
      num: 0,
      region: "",
      AppName: '',
      ASAInstance: {},
      SubList: [],
      ASAList: [],
      AppList: [],
      resourceGroup: [],
      regionList: [],
      JavaVersionList: [17,11,8]
    }
  },
  methods: {
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

      axios.get("/getResourceGroups/"+subId)
          .then(res => {
            console.log(res.data);
            this.resourceGroup = res.data;
          })
          .catch(err => {
            console.log(err);
          })

      axios.get("/getRegionList")
          .then(res => {
            console.log(res.data);
            this.regionList = res.data;
          })
          .catch(err => {
            console.log(err);
          })
    },
    changeAppInstance(ASAName){
      for (const instance of this.ASAList) {
        if(instance.name === ASAName) {
            this.ASAInstance = instance;
        }
      }
      axios.get("/getAppList?" + "subscriptionId=" + this.subId  + "&rgName=" + this.ASAInstance.resourceGroupName + "&serviceName=" + this.ASAInstance.name)
          .then(res => {
            console.log(res.data);
            this.AppList = res.data;
          })
          .catch(err => {
            console.log(err);
          })

    },
    deployCode(){
      this.showProgress = true;
      this.showProgress = true;
      for(let i=0; i<=99; i++){
        setTimeout(() => {this.num = i}, 2000);
      }

      const action = document.getElementById("action");
      const enableAction = action.checked === true;
      const url = "url=" + this.url + "&subscriptionId=" + this.subId + "&rgName=" + this.ASAInstance.resourceGroupName + "&serviceName=" + this.ASAInstance.name + "&javaVersion=" + this.JavaVersion + "&appName=" + this.AppName + "&enableAction=" + enableAction  + "&region=" + this.ASAInstance.region;
      axios.get("/getRepo?"+url)
          .then(res => {
            if(res.data === "true" || res.data === true){
             alert("Done!")
            }
            this.num = 100;
            document.getElementById("viewEndpoint").disabled = false;
            document.getElementById("viewOnPortal").disabled = false;
          })
          .catch(err => {
            alert(err);
          })

    },
    createAndDeploy(){
      this.showProgress = true;
      for(let i=0; i<=99; i++){
        setTimeout(() => {this.num = i}, 2000);
      }

      const action = document.getElementById("createAction");
      const enableAction = action.checked === true;
      const instanceName = document.getElementById("instanceName").value;
      const appName = document.getElementById("appName").value;
      const url = "url=" + this.url + "&subscriptionId=" + this.subId + "&rgName=" + this.resourceGroupName + "&serviceName=" + instanceName + "&javaVersion=" + this.JavaVersion + "&appName=" + appName + "&enableAction=" + enableAction  + "&region=" + this.RegionName;
      axios.get("/getRepo?"+url)
          .then(res => {
            if(res.data === "true" || res.data === true){
              alert("Create and deploye code successfully!");
            }
            this.num = 100;
            document.getElementById("viewEndpoint").disabled = false;
            document.getElementById("viewOnPortal").disabled = false;
          })
          .catch(err => {
            alert(err);
          })
    },
    viewEndpoint(){
      const endpoint = "https://"+this.ASAInstance.name+"-" +this.AppName +".azuremicroservices.io/";
      open(endpoint);
    },
    viewOnPortal(){
      const portalUrl = "https://ms.portal.azure.com/#@microsoft.onmicrosoft.com/resource/subscriptions/"+this.subId+"/resourceGroups/"+this.ASAInstance.resourceGroupName+"/providers/Microsoft.AppPlatform/Spring/"+this.ASAInstance.name+"/application_dashboard";
      open(portalUrl);
      },
    stringify: JSON.stringify,
  },
  beforeCreate(){
    if(this.gitRepoUrl !== null && this.gitRepoUrl !== '' && this.gitRepoUrl !== undefined) {
      sessionStorage.setItem("githubRepoUrl", this.gitRepoUrl);
    }
  },
  created() {
  this.querySubId();
  },
  beforeDestroy() {

  },

};
</script>