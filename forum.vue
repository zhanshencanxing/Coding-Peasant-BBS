<template>
	<div id="main-notes">
		<div class="main-search main-div" style="line-height: 3em;">
			<div style="width: 50%;display: inline-block;float: right;">	
				<el-input
					placeholder="请输入搜索内容"
					prefix-icon="el-icon-search"
					v-model="inputSearch" style="width: 60%">
				</el-input>
				<el-button type="primary" plain @click="GoSearch()">搜索</el-button>
			</div>
		</div>
		<div class="main-pages main-div" style="text-align: center;line-height: 3em;">
			<el-pagination
				background
				@size-change="handleSizeChange()"
				@current-change="handleCurrentChange()"
				:current-page.sync="currentPage"
				:page-size="pageSize"
				layout="prev, pager, next, jumper"
				:total="total" style="padding-top: 11px;">
			</el-pagination>
		</div>
		<div class="main-body">
			<template>
				<el-table
					:data="tableData"
					style="width: 90%;margin: 1em;">
					<el-table-column
						label="标题">
						<template slot-scope="scope">
							<p style="margin-left: 10px;font-weight: bold">
								<span @click="getNote(scope.row.forContent)">{{ scope.row.forTheme }}</span>
								<el-tag v-show="scope.row.elite==1" size="small" style="font-size: small;margin-left: 1em;color: darkorange;font-weight: bold;">
									<i class="el-icon-star-off" style="font-weight: bold;">精品</i>
								</el-tag>
								<el-tag v-show="scope.row.state==1" size="small" style="font-size: small;margin-left: 1em;color: red;font-weight: bold;">
									<i class="el-icon-medal" style="font-weight: bold;">置顶</i>
								</el-tag>
							</p>
							<el-badge :value="scope.row.commentCount" class="item" type="primary" style="margin-right: 1em;">
								<el-button size="small" icon="el-icon-chat-dot-round" style="font-size: small;">评论</el-button>
							</el-badge>
							<el-badge :value="scope.row.fabulousCount" class="item" type="primary">
								<el-button size="small" icon="el-icon-thumb" style="font-size: small;">赞</el-button>
							</el-badge>
						</template>
					</el-table-column>
					<el-table-column
						prop="userName"
						label="作者"
						width="150">
					</el-table-column>
					<el-table-column
						label="发表时间"
						width="230">
						<template slot-scope="scope">
							<i class="el-icon-time"></i>
							<span style="margin-left: 10px">{{ scope.row.forTime }}</span>
						</template>
					</el-table-column>
				</el-table>
			</template>
		</div>
		<div class="main-pages main-div" style="text-align: center;line-height: 3em;">
			<el-pagination
				background
				@size-change="handleSizeChange()"
				@current-change="handleCurrentChange()"
				:current-page.sync="currentPage"
				:page-size="pageSize"
				layout="prev, pager, next, jumper"
				:total="total" style="padding-top: 11px;">
			</el-pagination>
		</div>
		
		<div id="editNote">
			<div style="margin-top: 8px;border: 1px #cfcfcf solid">
				<div style="font-size: 15px;height: 2.5em;line-height: 2.5em;font-weight: bold;padding-left: 1em;background-color: #EDEDED">编辑帖子</div>
				<div style="padding: 1em;">
					<el-input v-model="forTheme" size="small" :maxlength="ntitleLength" placeholder="请输入帖子标题" style="width: 300px"></el-input>
					还可输入 <font style="font-weight: bold">{{ntitleLength-forTheme.length}}</font> 个字符
				</div>
				
				<div class="edit_container">
					<quill-editor 
						v-model="content" 
						ref="myQuillEditor" 
						:options="editorOption" 
						@blur="onEditorBlur($event)" 
						@focus="onEditorFocus($event)"
						@change="onEditorChange($event)">
					</quill-editor>
				</div>
				
				<el-button @click="getHtml()" style="margin: 1em">发表帖子</el-button>
			</div>
		</div>
	</div>
</template>

<script>
	export default {
		data() {
			return {
				inputSearch:"",
                currentPage:1,
                pageSize:20,
				total: 129,
                tableData: [{
                    forId: 5,
                    forTheme: "biaotititle",
                    forTime: "2018-01-01 08:12:23",
                    userId:1,
                    fabulousCount:12,
                    forContent:"b5aa2479-67e8-4069-91a7-17003a59453c.txt",
                    exaState:2,
                    elite:1,
                    state:1,
					userName:'管理员',
					commentCount: 99
                },{
                    forId: 5,
                    forTheme: "biaotititle",
                    forTime: "2018-01-01 08:12:23",
                    userId:1,
                    fabulousCount:8,
                    forContent:"b5aa2479-67e8-4069-91a7-17003a59453c.txt",
                    exaState:2,
                    elite:1,
                    state:0,
					userName:'管理员',
					commentCount: 67
                },{
                    forId: 5,
                    forTheme: "biaotititle",
                    forTime: "2018-01-01 08:12:23",
                    userId:1,
                    fabulousCount:2,
                    forContent:"b5aa2479-67e8-4069-91a7-17003a59453c.txt",
                    exaState:2,
                    elite:0,
                    state:0,
					userName:'管理员',
					commentCount: 33
                }],
                forTheme:"",
                ntitleLength:50,
				content: "<p>example content</p><p>您需要登录后才可以发帖</p></br></br></br></br></br></br>",
				editorOption: {}
			};
		},
		methods:{
			getNote(forContent){
				
			},
			GoSearch(){
				// 后端网址
				var url = "http://172.25.20.?/forum/search";
				// 传递给后端的数据
				var data = {ntitle: this.inputSearch};
				this.$axios.get(url,this.$qs.stringify(data),{
					headers: {
						'Content-Type':'application/x-www-form-urlencoded; charset=UTF-8'
					}
				}).then(res=>{
					this.tableData = res.data;
				})
			},
			
			onEditorReady(editor) {}, // 准备编辑器
			onEditorBlur(){}, // 失去焦点事件
			onEditorFocus(){}, // 获得焦点事件
			onEditorChange(event){
				console.log('onEditorChange'+this.content);
			} // 内容改变事件
		},
		mounted:function(){
			this.GoSearch();
		}
	}
</script>

<style>
	#main-notes{
		width: 90%;
		margin: 0 auto;
	}
	.main-div{
		width: 100%;
		height: 3em;
		margin-top: 8px;
		border: 1px #cfcfcf solid;
	}
	.main-body table tr td:first-child p:hover{
		color: #3a8ee6;
		cursor: pointer;
	}
</style>