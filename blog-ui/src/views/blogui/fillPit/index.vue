<template style="height: 100%">
  <div class="app-container pit">
    <div>
      <el-collapse v-model="tagShow" accordion @change="changeTag">
        <el-collapse-item v-for="tag in pitTags" :title="tag" :name="tag">
          <div class="pit-container" v-if="pitList[tag]!=undefined&&pitList[tag].length>0">
            <el-table
              :data="pitList[tag]"
              style="width: 100%"
              header-row-style="display:none">
              <el-table-column
                prop="content">
              </el-table-column>
            </el-table>
          </div>
        </el-collapse-item>
      </el-collapse>
    </div>


  </div>
</template>

<script>
import {listPits, getPitTags} from "@/api/business/salish";
import Editor from '@/components/Editor';
import infiniteScroll from "@/views/dashboard/mixins/infiniteScroll";

export default {
  name: "Blog",
  components: {
    Editor,
  },
  mixins: [infiniteScroll],
  data() {
    return {
      // 总条数
      total: 0,
      // 填坑表格数据
      pitList: {},
      pitTags: [],
      tagShow: '',
      // 查询参数
      queryParams: {
        tag: undefined,
      },
    };
  },
  created() {
    this.getPitTags();
    // this.getList();
  },
  methods: {
    getPitTags() {
      getPitTags().then(res => {
        this.pitTags = res.data;
        if (this.pitTags.length > 0) {
          this.tagShow = this.pitTags[0]
          this.queryParams.tag = this.pitTags[0];
          this.getList();
        }
      })
    },
    changeTag(tag) {
      if (tag != undefined && tag != '') {
        this.queryParams.tag = tag;
        if (this.pitList[tag] == undefined || this.pitList[tag] == []) {
          this.getList();
        }
      }
    },
    /** 查询博客列表 */
    getList() {
      listPits(this.queryParams).then(response => {
        // this.pitList[this.queryParams.tag] = response.rows;
        this.$set(this.pitList, this.queryParams.tag, response.rows)
      });
    },
    openPit(id) {

    }
  }
};
</script>
<style>
.pit .el-collapse-item__content {
  padding-bottom: 0;
}

</style>
<style scoped>
.pit {
  min-height: calc(100vh - 100px);
  background-color: white;
  padding: 10px;
  margin: 10px
}

.pit-container {
  border-radius: 2px;
  background-color: #FFFFFF;
  margin: 10px;
  padding: 10px;
  min-height: 60px;
}

</style>
