<template>
    <div class="app-container">
        <div class="app-content card wrapper">
          <el-tabs
            v-model="activeName"
            type="border-card"
            class="treeTabs"
          >
            <el-tab-pane class="treeWrapper" label="指挥单元" name="first">
              <div class="treeHeader">
                <el-input class="search-input" v-model="filterText" placeholder="请输入关键字查询" />
              </div>
              <el-tree class="treeContent" 
                      :data="dataSource" 
                      node-key="id" 
                      default-expand-all 
                      :expand-on-click-node="false" 
                      ref='tree1' 
                      :allow-drag="returnFalse" 
                      :filter-node-method="filterNode">
                  <template #default="{ node, data }">
                      <span :id='data.id' draggable="true" @dragstart="handleNodeDragStart">{{ node.label }}</span>
                  </template>
              </el-tree>
            </el-tab-pane>
            <el-tab-pane class="treeWrapper" label="平台" name="second">
              <div class="treeHeader">
                <el-input class="search-input" v-model="filterText2" placeholder="请输入关键字查询" />
              </div>
              <el-tree class="treeContent" 
                      :data="dataSource2" 
                      node-key="id" 
                      default-expand-all 
                      :expand-on-click-node="false" 
                      ref='tree2' 
                      :allow-drag="returnFalse" 
                      :filter-node-method="filterNode">
                  <template #default="{ node, data }">
                      <span :id='data.id' draggable="true" @dragstart="handleNodeDragStart">{{ node.label }}</span>
                  </template>
              </el-tree>
            </el-tab-pane>
          </el-tabs>
          <div class="moveWrapper">
            <span>
              <el-input-number
                v-model="moveNum"
                :min="1"
                :max="10"
                size="small"
                controls-position="right"
              />
            </span>
            <span style="padding-left: 5px;">
              >>
            </span>
          </div>
          <el-tabs
            v-model="activeName2"
            type="border-card"
            class="treeTabs"
          >
            <el-tab-pane class="treeWrapper" label="编成树" name="three">
                <div class="treeHeader">
                  <el-input class="search-input" v-model="filterText3" placeholder="请输入关键字查询" />
                </div>
                <el-tree class="treeContent" 
                        :data="dataSource3" 
                        node-key="id" 
                        default-expand-all 
                        :expand-on-click-node="false" 
                        ref='tree3' 
                        draggable
                        :allow-drop="allowDrop"
                        :filter-node-method="filterNode">
                    <template #default="{ node, data }">
                        <el-input v-model='data.label' size="small" v-if='data.edit' @blur='data.edit = false'></el-input>
                        <span :id='data.id' @drop="drop_handler" @dragover="dragover_handler" v-else >{{ node.label }}</span>

                        <span style='margin-left: 20px;' class='tree-btn' v-if="data.parentId != 0">
                            <el-button @click="data.edit=true" size="small" text type="success">重命名</el-button>
                            <!-- <el-button @click="clone(node, data)" size="small" v-if="data.id != 0" text type="success">克隆</el-button> -->
                            <el-button @click="remove(node, data)" size="small" v-if="data.id != 0" text type="success">删除</el-button>
                        </span>
                    </template>
                </el-tree>
            </el-tab-pane>
          </el-tabs>
        </div>
    </div>
 </template>
 
<script lang="ts" setup>
import {getCurrentInstance, onMounted, ref, toRefs, watch} from 'vue'
  import { ElTabPane, ElTree } from 'element-plus'
  import type Node from 'element-plus/es/components/tree/src/model/node'
  const { proxy } = getCurrentInstance()as any

  const { scenario_composition_camp } = proxy.useDict("scenario_composition_camp");

  const moveNum = ref(1)
  const activeName = ref("first")
  const activeName2 = ref("three")
  const filterText = ref('')
  const filterText2 = ref('')
  const filterText3 = ref('')
  const tree1=ref<InstanceType<typeof ElTree>>()
  const tree2=ref<InstanceType<typeof ElTree>>()
  const tree3=ref<InstanceType<typeof ElTree>>()

  interface Tree {
    id: number
    type: string
    url: string
    camp: string
    label: string
    parentId: number
    belongTo: string
    children?: Tree[],
    flag?: 'entity'
    edit?: false
  }
  const props = defineProps({
    dataSource: {
      type: Array,
      default: () => []
    },
    dataSource2: {
      type: Array,
      default: () => []
    },
    dataSource3: {
      type: Array,
      default: () => []
    }
  });

  const {dataSource,dataSource2,dataSource3} = toRefs(props)

  watch(filterText, (val) => {
      tree1.value!.filter(val)
  })
  watch(filterText2, (val) => {
      tree2.value!.filter(val)
  })
  watch(filterText3, (val) => {
      tree3.value!.filter(val)
  })

  const filterNode = (value: string, data: Tree) => {
      if (!value) return true
      return data.label.includes(value)
  }

  const returnFalse = () => {
    return false
  }

  const allowDrop = (draggingNode: Tree) => {
    if (draggingNode.data.id == 0) {
      return false
    }
    return true
  }

  const handleNodeDragStart=(ev) => {
    ev.dataTransfer.dropEffect="copy";
    ev.dataTransfer.setData("text/plain",ev.target.id);

    let node = tree1.value!.getNode(Number(ev.target.id))
    if (activeName.value==='second') {
      node = tree2.value!.getNode(Number(ev.target.id))
    }
    if (node.data.children.length > 0) {
      proxy.$modal.msgError('只能拖动叶子节点！')
      return
    }
  }

  function dragover_handler(ev) {
    ev.preventDefault();
    ev.dataTransfer.dropEffect="link";
  }

  function drop_handler(ev) {
    ev.preventDefault();

    let outId=ev.dataTransfer.getData("text/plain");
    let inId=ev.target.id
      console.log('outId',outId,'inId',inId)

    // 获取拖拽节点
    let outNode=tree1.value!.getNode(Number(outId))
    if (activeName.value==='second') {
      outNode = tree2.value!.getNode(Number(outId))
    }

    // 只能拖动叶子节点！
    if (outNode.data.children.length > 0) {
      return
    }

    for(let i=1;i<=moveNum.value;i++) {
      let newNode = {
        id: randomNum(),
        label: outNode.data.label +"_" + i,
        camp: outNode.data.camp,
        children: [],
        edit: outNode.data.edit, 
        type: outNode.data.type,
        url: outNode.data.url, 
        parentId: inId, 
        belongTo: 'tree3'
      }

      tree3.value!.append(newNode,Number(inId))
    }
    console.log(tree1.value!.data)
    console.log(tree3.value!.data)
  }

  function randomNum() {
    let date = new Date();
    let timestamp = date.getTime();
    let random = Math.floor(100 * Math.random()) + Math.floor(10 * Math.random()) + Math.random();
    return timestamp + Math.round(random);
  }

  const remove=(node: Node,data: Tree) => {
    proxy.$modal.confirm('是否确认删除名称为"' + data.label + '"的节点?').then(function () {
      let parent=node.parent
      let children: Tree[]=parent.data.children||parent.data
      let index=children.findIndex((d) => d.id===data.id)
      children.splice(index,1)
      dataSource.value=[...dataSource.value]
    }).catch(() => {});
  }
</script> 

<style scoped lang="scss">
.card {
  overflow-x: unset !important;
}

.wrapper {
    display: flex;
    justify-content: space-between;
    width: 100%;
    height: calc(80vh - 104px);

    .treeTabs {
      width: calc(50% - 50px);
    }

    .moveWrapper {
      width: 100px;
      display: flex;
      justify-content: center;
      align-items: center;
      
      :deep(.el-input-number--small) {
        width: 70px;
      }
    }

    .treeWrapper {
        .treeHeader {
          display: flex;
          justify-content: space-between;
          .search-input {
              width: calc(100% - 140px);
          }
        }
        .treeContent {
            height: calc(80vh - 235px);
            overflow-y: auto;
        }
    }
}
.el-tree-node__content {
  .tree-btn {
    display: none;
  }
}

.el-tree-node__content:hover {
  .tree-btn {
    display: block;
  }
}
</style>