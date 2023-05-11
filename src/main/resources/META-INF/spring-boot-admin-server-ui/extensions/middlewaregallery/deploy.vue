<template xmlns="http://www.w3.org/1999/html">
  <div class="selected-asa" style="width: 60%;padding: 5px;margin: 10px auto;">

    <div class="mb-3 row">
      <label for="repo" class="col-sm-4 col-form-label" style="font-size: 16px;font-weight: normal;">GitHub
        repository address:</label>
      <div class="col-sm-7">
        <input type="text" class="form-control"
               style="background-color: #F5F5F5; border: 1px solid #ced4da;" v-model="url"
               disabled/>
      </div>
    </div>

<!--    <div class="mb-3 row">-->
<!--      <label for="branch" class="col-sm-4 col-form-label"-->
<!--             style="font-size: 16px;font-weight: normal;">GitHub repository Branch:</label>-->
<!--      <div class="col-sm-7">-->
<!--        <input type="text" class="form-control"-->
<!--               style="background-color: white; border: 1px solid #ced4da;" v-model="branch"/>-->
<!--      </div>-->
<!--    </div>-->

    <div class="mb-3 row">
      <label for="selectSubId" class="col-sm-4 col-form-label"
             style="font-size: 16px;font-weight: normal;">Subscription:<span
          style="color:red; display:inline-block;margin-right: 10px; margin-left: 5px;"> * </span>
        <svg style="display:inline-block" xmlns="http://www.w3.org/2000/svg" width="16" height="16"
             fill="currentColor"
             class="bi bi-exclamation-circle" viewBox="0 0 16 16">
          <title>An Azure subscription grants you access to Azure services. Your subscription is
            also how resource usage is reported and services are billed.</title>
          <path d="M8 15A7 7 0 1 1 8 1a7 7 0 0 1 0 14zm0 1A8 8 0 1 0 8 0a8 8 0 0 0 0 16z"/>
          <path
              d="M7.002 11a1 1 0 1 1 2 0 1 1 0 0 1-2 0zM7.1 4.995a.905.905 0 1 1 1.8 0l-.35 3.507a.552.552 0 0 1-1.1 0L7.1 4.995z"/>
        </svg>
      </label>
      <div class="col-sm-7">
        <select class="form-control" v-model="subId" id="selectSubId" name="Subscription"
                @change="changeASAInstance(subId)">
          <option v-for="item in SubList" :key="item.name" :value="item.id">
            {{ item.name }}
          </option>
        </select>
      </div>
    </div>

    <div class="mb-3 row">
      <label for="SelectResourceGroup" class="col-sm-4 col-form-label"
             style="font-size: 16px;font-weight: normal;">
        <div class="vl"
             style=" display:inline-block; margin: -25px 0 5px 10px;border-left: 1px solid #C0C0C0;height: 40px;width: 40px;border-bottom: 1px solid #C0C0C0;"></div>
        Resource group:<span
          style="color:red; display:inline-block;margin-right: 10px;margin-left: 5px;"> * </span>
        <svg style="display:inline-block" xmlns="http://www.w3.org/2000/svg" width="16" height="16"
             fill="currentColor"
             class="bi bi-exclamation-circle" viewBox="0 0 16 16">
          <title>A resource group is a collection of resources that share the same lifecycle,
            permission, and policies.</title>
          <path d="M8 15A7 7 0 1 1 8 1a7 7 0 0 1 0 14zm0 1A8 8 0 1 0 8 0a8 8 0 0 0 0 16z"/>
          <path
              d="M7.002 11a1 1 0 1 1 2 0 1 1 0 0 1-2 0zM7.1 4.995a.905.905 0 1 1 1.8 0l-.35 3.507a.552.552 0 0 1-1.1 0L7.1 4.995z"/>
        </svg>
      </label>
      <div class="col-sm-7">
        <select class="form-control" v-model="resourceGroupName" id="SelectResourceGroup"
                @change="getASAInstanceByResourceGroupName(resourceGroupName)">
          <option v-for="item in ResourceGroup" :key="item.name" :value="item.name">
            {{ item.name }}
          </option>
        </select>
        <span><a data-toggle="modal" data-target="#createModal" style="color: #0366d6; cursor: pointer;">Create new</a></span>
      </div>
    </div>

    <div class="mb-3 row">
      <label for="SelectAsaInstance" class="col-sm-4 col-form-label"
             style="font-size: 16px;font-weight: normal;">Service Instance:<span
          style="color:red; display:inline-block;margin-right: 10px;margin-left: 5px;"> * </span></label>
      <div class="col-sm-7">
        <select class="form-control" v-model="ASAName" id="SelectAsaInstance"
                placeholder="Please select the Asa you want to use"
                @change="changeAppInstance(ASAName)">
          <option v-for="item in ASAInstanceList" :key="item.name" :value="item.name">
            {{ item.name }}
          </option>
        </select>
      </div>
    </div>

<!--    <div class="mb-3 row">-->
<!--      <label for="SelectAppInstance" class="col-sm-4 col-form-label"-->
<!--             style="font-size: 16px;font-weight: normal;">App Instance:<span-->
<!--          style="color:red; display:inline-block;margin-right: 10px;margin-left: 5px;"> * </span></label>-->
<!--      <div class="col-sm-7">-->
<!--        <select class="form-control" v-model="AppName"-->
<!--                placeholder="Please select the App you want to use" id="SelectAppInstance"-->
<!--                @change="changeAppInstanceDetails(AppName)">-->
<!--          <option v-for="item in AppList" :key="item.name" :value="item.name">-->
<!--            {{ item.name }}-->
<!--          </option>-->
<!--        </select>-->
<!--      </div>-->
<!--    </div>-->

    <div class="mb-3 row">
      <label for="App name" class="col-sm-4 col-form-label"
             style="font-size: 14px;font-weight: bold;">Application details:</label>
    </div>

    <div class="mb-3 row">
      <label for="App name" class="col-sm-3 col-form-label" style="font-size: 14px;">App
        name:</label>
      <div class="col-sm-5">
        <input type="text" class="form-control" style="background-color: #F5F5F5; border: none;"
               v-model="AppInstance.name" readonly/>
      </div>
    </div>

    <div class="mb-3 row">
      <label for="Runtime platform" class="col-sm-3 col-form-label" style="font-size: 14px;">Runtime
        platform:</label>
      <div class="col-sm-5">
        <input type="text" class="form-control" style="background-color: white; border: none;"
               v-model="AppInstance.runtimePlatform" readonly/>
      </div>
    </div>

    <div class="mb-3 row">
      <label for="Image" class="col-sm-3 col-form-label" style="font-size: 14px;">Image:</label>
      <div class="col-sm-5">
        <input type="text" class="form-control" style="background-color: white; border: none;"
               v-model="AppInstance.image" readonly/>
      </div>
    </div>

    <div class="mb-3 row">
      <label for="vCPU" class="col-sm-3 col-form-label" style="font-size: 14px;">vCPU:</label>
      <div class="col-sm-5">
        <input type="text" class="form-control" style="background-color: white; border: none;"
               v-model="AppInstance.cpu" readonly/>
      </div>
    </div>

    <div class="mb-3 row">
      <label for="Memory" class="col-sm-3 col-form-label" style="font-size: 14px;">Memory:</label>
      <div class="col-sm-5">
        <input type="text" class="form-control" style="background-color: white; border: none;"
               v-model="AppInstance.memoryInGB" readonly/>
      </div>
    </div>

    <div class="mb-3 row">
      <label for="Instance count" class="col-sm-3 col-form-label" style="font-size: 14px;">Instance
        count:</label>
      <div class="col-sm-5">
        <input type="text" class="form-control" style="background-color: white; border: none;"
               v-model="AppInstance.instanceCount" readonly/>
      </div>
    </div>

    <div class="mb-3 row">
      <label for="Deployment type" class="col-sm-3 col-form-label" style="font-size: 14px;">Deployment
        type:</label>
      <div class="col-sm-5">
        <input type="text" class="form-control" style="background-color: white; border: none;"
               v-model="AppInstance.deploymentType" readonly/>
      </div>
    </div>

    <div class="form-group">
      <label>
        <input class="mui-switch" id="action" type="checkbox"> Enable GitHub Action
      </label>
    </div>

    <div class="form-group">
      <button class="btn btn-primary" data-toggle="modal" :data-target="modalId" @click="deployCode()">Deploy</button>
    </div>

  </div>

  <!--Create App page-->
  <div class="modal fade" id="createModal" tabindex="-1" role="dialog" aria-labelledby="modalLabel" aria-hidden="true">
    <div class="modal-dialog modal-lg">
      <div class="modal-content">
        <div class="modal-header">
          <span style="font-size: 20px;font-weight: bold;">Create Azure Spring App and deploy source
            code</span>
          <button type="button" class="close" data-dismiss="modal" aria-label="Close">
            <span aria-hidden="true">&times;</span>
          </button>
        </div>
        <div class="modal-body">
          <div class="mb-3 row">
            <label for="TypeResourceGroup" class="col-sm-6 col-form-label" style="font-size: 16px;">Resource
              group name:<span
                  style="color:red; display:inline-block;margin-right: 10px;margin-left: 5px;"> * </span></label>
            <div class="col-sm-6">
              <input type="text" class="form-control" style="border: 1px solid #ced4da;"
                     v-model="NewResourceGroupName"/>
            </div>
          </div>


          <div class="mb-3 row">
            <label for="TypeAppInstance" class="col-sm-6 col-form-label" style="font-size: 16px;">Service instance Name:<span
                  style="color:red; display:inline-block;margin-right: 10px;margin-left: 5px;"> * </span></label>
            <div class="col-sm-6">
              <input type="text" class="form-control" style="border: 1px solid #ced4da;"
                     v-model="NewInstanceName"/>
            </div>
          </div>

          <div class="mb-3 row">
            <label for="TypeAppInstance" class="col-sm-6 col-form-label" style="font-size: 16px;">App
              Instance Name:<span
                  style="color:red; display:inline-block;margin-right: 10px;margin-left: 5px;"> * </span></label>
            <div class="col-sm-6">
              <input type="text" class="form-control" style="border: 1px solid #ced4da;"
                     v-model="NewAppName"/>
            </div>
          </div>

          <div class="mb-3 row">
            <label for="SelectRegion" class="col-sm-6 col-form-label" style="font-size: 16px;">Select
              Region:<span
                  style="color:red; display:inline-block;margin-right: 10px;margin-left: 5px;"> * </span></label>
            <div class="col-sm-6">
              <select class="form-control" v-model="RegionName" id="SelectRegion">
                <option v-for="item in RegionList" :key="item.label" :value="item.name">
                  {{ item.label }}
                </option>
              </select>
            </div>
          </div>

          <div class="mb-3 row">
            <label for="SelectJavaVersion" class="col-sm-6 col-form-label" style="font-size: 16px;">Select
              Java version:<span
                  style="color:red; display:inline-block;margin-right: 10px;margin-left: 5px;"> * </span></label>
            <div class="col-sm-6">
              <select class="form-control" v-model="CreateJavaVersion"
                      placeholder="Please select the Java version you want to use">
                <option v-for="item in JavaVersionList" :key="item" :value="item">
                  {{ item }}
                </option>
              </select>
            </div>
          </div>

          <div class="form-group">
            <label>
              <input class="mui-switch" id="createAction" type="checkbox"> Enable GitHub Action
            </label>
          </div>
        </div>
        <div class="modal-footer">
          <button type="button" class="btn btn-outline-primary"  data-dismiss="modal">Close</button>
          <button type="button" class="btn btn-outline-dark" data-toggle="modal" :data-target="modalId"  @click="createAndDeploy()">Create and Deploy</button>
        </div>
      </div>
    </div>
  </div>

  <!--Loading Page-->
  <div class="modal fade" id="loadModal" tabindex="-1" aria-labelledby="exampleModalLabel" aria-hidden="true">
    <div class="modal-dialog modal-xl">
      <div class="modal-content">
        <div class="modal-header">
          <span style="font-size: 20px;font-weight: bold;">Deployment page</span>
          <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
        </div>
        <div class="modal-body">

          <div class="col-sm-6" v-if="flag ==='create'">
            <label v-if="createRgTime < 100&&flag==='create'" >Creating Resource group: <code>{{NewResourceGroupName}}</code> </label>
            <label v-else>Resource group: <code>{{NewResourceGroupName}}</code> created.</label>
          </div>
          <div class="mb-3 row" v-if="flag ==='create'">
            <div class="col-sm-1" style="margin-left: 25px;margin-bottom: 10px;">
              <svg width="30px" height="30px" viewBox="0 0 10 10" v-if="showRgIcon">
                <g id="Page-1" stroke="none" stroke-width="1" fill="none" fill-rule="evenodd">
                  <g id="tick">
                    <circle id="Oval" fill="#349006" cx="5" cy="5" r="5"></circle>
                    <path d="M7.26241838,2.25 L8.35843389,3.34601551 L3.9390165,7.76543289 L3.937,7.763 L3.93041937,7.77093389 L1.65,5.49051452 L2.74601551,4.39449901 L3.932,5.58 L7.26241838,2.25 Z" id="Combined-Shape" fill="#FFFFFF"></path>
                  </g>
                </g>
              </svg>
            </div>
            <div class="col-sm-5" style="padding-top: 5px;">
              <div class="progress">
                <div class="progress-bar" role="progressbar" :style="{ width: createRgTime + '%'}" aria-valuemin="0" aria-valuemax="100">{{
                    createRgTime + '%'
                  }}</div>
              </div>
            </div>
          </div>

          <div class="col-sm-6" v-if="createRgTime === 100 &&flag==='create'">
            <label  v-if="createASATime < 100&&flag==='create'" >Creating Srevice: <code>{{NewInstanceName}}</code> </label>
            <label  v-else>Srevice: <code>{{NewInstanceName}}</code> created.</label>
          </div>
          <div class="mb-3 row" v-if="createRgTime === 100 &&flag==='create'">
            <div class="col-sm-1" style="margin-left: 25px;margin-bottom: 10px;">
              <svg width="30px" height="30px" viewBox="0 0 10 10" v-if="showASAIcon">
                <g id="Page-1" stroke="none" stroke-width="1" fill="none" fill-rule="evenodd">
                  <g id="tick">
                    <circle id="Oval" fill="#349006" cx="5" cy="5" r="5"></circle>
                    <path d="M7.26241838,2.25 L8.35843389,3.34601551 L3.9390165,7.76543289 L3.937,7.763 L3.93041937,7.77093389 L1.65,5.49051452 L2.74601551,4.39449901 L3.932,5.58 L7.26241838,2.25 Z" id="Combined-Shape" fill="#FFFFFF"></path>
                  </g>
                </g>
              </svg>
            </div>
            <div class="col-sm-5" style="padding-top: 5px;">
              <div class="progress">
                <div class="progress-bar" role="progressbar" :style="{ width: createASATime + '%'}" aria-valuemin="0" aria-valuemax="100">{{
                    createASATime + '%'
                  }}</div>
              </div>
            </div>
          </div>

          <div class="col-sm-6" v-if="createASATime === 100 &&flag==='create'">
            <label  v-if="createAppTime < 100&&flag==='create'" >Creating App: <code>{{NewAppName}}</code> </label>
            <label  v-else>App: <code>{{NewAppName}}</code> created.</label>
          </div>
          <div class="mb-3 row" v-if="createASATime === 100 &&flag==='create'">
            <div class="col-sm-1" style="margin-left: 25px;margin-bottom: 10px;">
              <svg width="30px" height="30px" viewBox="0 0 10 10" v-if="showAppIcon">
                <g id="Page-1" stroke="none" stroke-width="1" fill="none" fill-rule="evenodd">
                  <g id="tick">
                    <circle id="Oval" fill="#349006" cx="5" cy="5" r="5"></circle>
                    <path d="M7.26241838,2.25 L8.35843389,3.34601551 L3.9390165,7.76543289 L3.937,7.763 L3.93041937,7.77093389 L1.65,5.49051452 L2.74601551,4.39449901 L3.932,5.58 L7.26241838,2.25 Z" id="Combined-Shape" fill="#FFFFFF"></path>
                  </g>
                </g>
              </svg>
            </div>
            <div class="col-sm-5" style="padding-top: 5px;">
              <div class="progress">
                <div class="progress-bar" role="progressbar" :style="{ width: createAppTime + '%'}" aria-valuemin="0" aria-valuemax="100">{{
                    createAppTime + '%'
                  }}</div>
              </div>
            </div>
          </div>

          <div class="col-sm-6" v-if="createAppTime === 100 || flag === 'deploy'">
            <label v-if="deployTime < 100" >Deploying... </label>
            <label v-else>Deployment is completed</label>
          </div>
          <div class="mb-3 row" v-if="createAppTime === 100 || flag === 'deploy'">
            <div class="col-sm-1" style="margin-left: 25px;margin-bottom: 10px;">
              <svg width="30px" height="30px" viewBox="0 0 10 10" v-if="showDeployIcon">
                <g id="Page-1" stroke="none" stroke-width="1" fill="none" fill-rule="evenodd">
                  <g id="tick">
                    <circle id="Oval" fill="#349006" cx="5" cy="5" r="5"></circle>
                    <path d="M7.26241838,2.25 L8.35843389,3.34601551 L3.9390165,7.76543289 L3.937,7.763 L3.93041937,7.77093389 L1.65,5.49051452 L2.74601551,4.39449901 L3.932,5.58 L7.26241838,2.25 Z" id="Combined-Shape" fill="#FFFFFF"></path>
                  </g>
                </g>
              </svg>
            </div>
            <div class="col-sm-5" style="padding-top: 5px;">
              <div class="progress">
                <div class="progress-bar" role="progressbar" :style="{ width: deployTime + '%'}" aria-valuemin="0" aria-valuemax="100">{{
                    deployTime + '%'
                  }}</div>
              </div>
            </div>
          </div>

          <div class="col-sm-10">
            <label class="label-div">Next steps</label>
            <p class="text-left">Lorem ipsum dolor sif amet consectetur. Sagittis auctor netus suscipif
              lectus neque velit. leo lacus et adipiscing elif lectus aliquam mauris pharetra.
              Pellentesque sapien.</p>
            <br/>
            <p class="text-left">Et scelerisque mauris aliquet quis libero. Vulputate mattis cursus ut en
              auctor integer gravide suspendisse turpis.</p>
            <br/>
            <div class="col-sm-10">
              <ul>
                <li>Lorem eget arcu felis amet mauris id vitae ac.Imperdiet imperdiet.</li>
                <li>Consequat adipiscing faucibus feugiat. Eget nisi facliisis posuere risus.</li>
                <li>Et eu mauris eo. Uttrice morbi ament arcu semper morbi.</li>
                <li>Vulputate nibh lelmentum sit turpis donec sed.</li>
                <li>Proin felis.</li>
              </ul>
            </div>
          </div>
        </div>

          <div class="modal-footer" style="float: left">
            <button type="button" class="btn btn-outline-primary btn-xs"  data-dismiss="modal">Close</button>
            <button v-if="showView" id="viewEndpoint" class="btn btn-primary btn-xs" @click="viewEndpoint()">View the app</button>
            <button v-if="showView" id="viewOnPortal" class="btn btn-primary btn-xs" @click="viewOnPortal()">Go to Azure Spring Apps</button>
          </div>
      </div>
    </div>
    </div>

</template>

<style>

label {
  display: block;
  vertical-align: middle;
}

label, input, select {
  vertical-align: middle;
}
.label-div{
  font-size: 16px;
  font-weight: bold;
}
ul li{
  font-size: 14px;
  list-style-type: disc;
  margin-left: 14px;
}
code{
  color: blue;
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
import {toast} from "vue3-toastify";
import 'vue3-toastify/dist/index.css';

Vue.proto
export default {
  props: ['gitRepoUrl', 'branchName'],
  setup() {
    const applications = [{"name":"aa"}, {"name":"bb"}];
    return {
      applications,
    };
  },
  data() {
    return {
      url: sessionStorage.getItem("githubRepoUrl"),
      branch: sessionStorage.getItem("branchName"),
      flag: '',
      modalId: '',
      RegionName: '',
      resourceGroupName: "",
      showRgIcon: false,
      showASAIcon: false,
      showAppIcon: false,
      showView: false,
      showDeployIcon: false,
      deployTime: 0,
      deployTimer: null,
      createRgTime: 0,
      createRgTimer: null,
      createASATime: 0,
      createASATimer: null,
      createAppTime: 0,
      createAppTimer: null,
      subId: '',
      ASAName: "",
      JavaVersion: "",
      region: "",
      AppName: '',
      ASAInstance: {},
      ASAInstanceList: [],
      SubList: [],
      ASAList: [],
      AppList: [],
      ResourceGroup: [],
      RegionList: [],
      JavaVersionList: [17, 11, 8],
      AppInstance: {},
      NewInstanceName: '',
      NewAppName: '',
      NewResourceGroupName: '',
      CreateJavaVersion: '',
    }
  },
  methods: {
    querySubId() {
      axios.get('/subscriptionList')
          .then(res => {
            this.SubList = res.data;
            if(this.SubList.length === 0){
              toast.info("Not found Subscription",{ position: toast.POSITION.TOP_CENTER, autoClose: 2000,});
            }
          })
          .catch(err => {
            toast.error(err);
          })
    },
    getRegionList(){
      axios.get("/getRegionList")
          .then(res => {
            this.RegionList = res.data;
            if(this.RegionList.length === 0){
              toast.info("Not found Region",{ position: toast.POSITION.TOP_CENTER, autoClose: 2000,});
            }
          })
          .catch(err => {
            toast.error(err);
          })
    },
    changeASAInstance(subId) {
      axios.get("/instanceList/" + subId)
          .then(res => {
            this.ASAList = res.data;
          })
          .catch(err => {
            toast.error(err);
          })

      axios.get("/getResourceGroups/" + subId)
          .then(res => {
            this.ResourceGroup = res.data;
            if(this.ResourceGroup.length === 0){
              toast.info("Not found Resource group",{ position: toast.POSITION.TOP_CENTER, autoClose: 2000,});
            }
          })
          .catch(err => {
            toast.error(err);
          })
    },
    getASAInstanceByResourceGroupName(resourceGroupName) {
      this.ASAInstanceList = this.ASAList.filter(item => item.resourceGroupName === resourceGroupName);
      if(this.ASAInstanceList.length === 0){
        toast.info("Not found Azure Spring Apps",{ position: toast.POSITION.TOP_CENTER, autoClose: 2000,});
      }
    },
    changeAppInstanceDetails(AppName) {
      for (const instance of this.AppList) {
        if (instance.name === AppName) {
          this.AppInstance = instance;
          this.AppInstance.image = 'N/A';
          this.AppInstance.deploymentType = 'GitHub repo';
        }
      }
    },
    changeAppInstance(ASAName) {
      this.AppName = '';
      this.AppList = [];
      // Get ASA region
      for (const instance of this.ASAList) {
        if (instance.name === ASAName) {
          this.ASAInstance = instance;
          // this.AppInstance = instance;
          this.AppInstance.name = this.url.substring(this.url.lastIndexOf("/") + 1, this.url.length) + Math.floor(Math.random()*(9999-1000));
          this.AppInstance.runtimePlatform = "Java_17";
          this.AppInstance.image = 'N/A';
          this.AppInstance.cpu = 1;
          this.AppInstance.memoryInGB = 1;
          this.AppInstance.instanceCount = 1;
          this.AppInstance.deploymentType = 'GitHub repo';
        }
      }
      axios.get("/getAppList?" + "subscriptionId=" + this.subId + "&rgName=" + this.resourceGroupName + "&serviceName=" + this.ASAName)
          .then(res => {
            this.AppList = res.data;
            if(this.AppList.length === 0){
              toast.info("Not found App",{ position: toast.POSITION.TOP_CENTER, autoClose: 2000,});
            }
          })
          .catch(err => {
            toast.error(err);
          })
    },
    deployCode() {
      if(this.subId === '' || this.resourceGroupName === '' || this.ASAName === '' || this.AppInstance.name === '') {
        toast.warning("Please select required",{ position: toast.POSITION.TOP_CENTER, autoClose: 2000,});
        return;
      }
      this.modalId = "#loadModal";
      this.flag = "deploy";
      this.showView = false;
      this.showDeployIcon = false;
      this.deployTime = 0;
      if (this.AppInstance.runtimePlatform === "Java_17") {
        this.JavaVersion = 17;
      } else if (this.AppInstance.runtimePlatform === "Java_11") {
        this.JavaVersion = 11;
      } else {
        this.JavaVersion = 8;
      }
      const action = document.getElementById("action");
      const enableAction = action.checked === true;
      const url = "url=" + this.url + "&subscriptionId=" + this.subId + "&rgName=" + this.resourceGroupName + "&serviceName=" + this.ASAName + "&javaVersion=" + this.JavaVersion + "&appName=" + this.AppInstance.name + "&enableAction=" + enableAction + "&region=" + this.ASAInstance.region + "&branchName=" + this.branch;
      axios.get("/deployApp?" + url)
          .then(res => {
            if (res.data === "done") {
              this.showView = true;
              this.showDeployIcon = true;
              this.deployTime = 100;
              this.modalId = "";
              toast.success("Deploy done!",{ position: toast.POSITION.TOP_CENTER, autoClose: 2000,});
            } else {
              toast.error(res.data,{ position: toast.POSITION.TOP_CENTER, autoClose: 2000,});
            }
          })
    },
    createAndDeploy() {
      if(this.subId === '' || this.NewResourceGroupName === '' || this.NewInstanceName === '' || this.NewAppName === '' || this.RegionName === '' || this.CreateJavaVersion === '') {
        toast.warning("Please type required",{ position: toast.POSITION.TOP_CENTER, autoClose: 2000,});
        return;
      }
      this.modalId = "#loadModal";
      this.flag = "create";
      this.showView = false;
      this.showDeployIcon = false;
      this.showRgIcon = false;
      this.showASAIcon = false;
      this.showAppIcon = false;
      this.deployTime = 0;
      this.createRgTime = 0;
      this.createASATime = 0;
      this.createAppTime = 0;
      const action = document.getElementById("createAction");
      const enableAction = action.checked === true;
      const url = "url=" + this.url + "&subscriptionId=" + this.subId + "&rgName=" + this.NewResourceGroupName + "&serviceName=" + this.NewInstanceName + "&javaVersion=" + this.CreateJavaVersion + "&appName=" + this.NewAppName + "&enableAction=" + enableAction + "&region=" + this.RegionName + "&branchName=" + this.branch;
      axios.get("/deployApp?" + url)
          .then(res => {
            if (res.data === "done") {
              this.showView = true;
              this.showDeployIcon = true;
              this.deployTime = 100;
              this.modalId = "";
              toast.success("Create and deploy done!",{ position: toast.POSITION.TOP_CENTER, autoClose: 2000,});
            }else {
              toast.error(res.data,{ position: toast.POSITION.TOP_CENTER, autoClose: 2000,});
            }
          })
    },
    queryRgProgress(){
      axios.get("/queryRgProgress?" + "subscriptionId=" + this.subId + "&rgName=" + this.NewResourceGroupName)
          .then(res => {
            if (res.data === true) {
              this.showRgIcon = true;
              this.createRgTime = 100;
            }
          })
    },
    queryASAProgress(){
      axios.get("/queryASAProgress?" + "subscriptionId=" + this.subId + "&rgName=" + this.NewResourceGroupName + "&serviceName=" + this.NewInstanceName)
          .then(res => {
            if (res.data === true) {
              this.showASAIcon = true;
              this.createASATime = 100;
            }
          })
    },
    queryAppProgress(){
      axios.get("/queryAppProgress?" + "subscriptionId=" + this.subId + "&rgName=" + this.NewResourceGroupName + "&serviceName=" + this.NewInstanceName + "&appName=" + this.NewAppName)
          .then(res => {
            if (res.data === true) {
              this.showAppIcon = true;
              this.createAppTime = 100;
            }
          })
    },
    viewEndpoint(){
      let endpoint;
      if(this.flag === "deploy") {
         endpoint = "https://" + this.ASAInstance.name + "-" + this.AppInstance.name + ".azuremicroservices.io/";
      } else {
         endpoint = "https://" + this.NewInstanceName + "-" + this.NewAppName + ".azuremicroservices.io/";
      }
      open(endpoint);
    },
    viewOnPortal(){
      let portalUrl;
      if(this.flag === "deploy") {
        portalUrl = "https://ms.portal.azure.com/#@microsoft.onmicrosoft.com/resource/subscriptions/"+this.subId+"/resourceGroups/"+this.resourceGroupName + "/providers/Microsoft.AppPlatform/Spring/" + this.ASAName + "/application_dashboard";
      } else {
        portalUrl = "https://ms.portal.azure.com/#@microsoft.onmicrosoft.com/resource/subscriptions/"+this.subId+"/resourceGroups/"+this.NewResourceGroupName + "/providers/Microsoft.AppPlatform/Spring/" + this.NewInstanceName + "/application_dashboard";
      }
      open(portalUrl);
      },
    stringify: JSON.stringify,
  },
  mounted() {
    this.createRgTimer = setInterval(() => {
      if (this.createRgTime !== 100 && this.flag === 'create') {
        this.queryRgProgress();
        if(this.createRgTime < 99) {
          this.createRgTime++;
        }
      }
    }, 2000);
    this.createASATimer = setInterval(() => {
      if (this.createASATime !== 100 && this.createRgTime === 100) {
        this.queryASAProgress();
        if (this.createASATime < 99) {
          this.createASATime++;
        }
      }
    }, 2000);
    this.createAppTimer = setInterval(() => {
      if (this.createAppTime !== 100 && this.createASATime === 100) {
        this.queryAppProgress();
        if (this.createAppTime < 99) {
          this.createAppTime++;
        }
      }
    }, 2000);
    this.deployTimer = setInterval(() => {
      if ((this.deployTime < 99 && this.createAppTime ===100) || (this.flag === 'deploy' && this.deployTime < 99)) {
        this.deployTime++;
      }
    }, 2000);
  },
  beforeCreate() {
    if (this.gitRepoUrl !== null) {
      sessionStorage.setItem("githubRepoUrl", this.gitRepoUrl);
      if(this.branchName !== null) {
        sessionStorage.setItem("branchName", this.branchName);
      }
    }
  },
  created() {
    this.querySubId();
    this.getRegionList();
  },
  beforeDestroy() {
    clearInterval(this.deployTimer);
    clearInterval(this.createRgTimer);
    clearInterval(this.createASATimer);
    clearInterval(this.createAppTimer);
  },

};
</script>