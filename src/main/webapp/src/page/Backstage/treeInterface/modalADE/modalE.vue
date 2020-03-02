/**
*
*  @author ZTiger
*
*/


<template>
    <Modal v-model="modalFlag" width="500" :mask-closable='false'>
        <p slot="header" class="know-modal-header">
            <Icon type="ios-information-circle"></Icon>
            <span>&nbsp;&nbsp;编&nbsp;辑&nbsp;节&nbsp;点</span>
        </p>
        <div class="know-modal-text">
            <p>编&nbsp;辑&nbsp;:&nbsp;<strong>{{selectNodeName.length>20?selectNodeName.substr(0,20):selectNodeName}}</strong></p>
            <p><Input type="text" id='modalExitInput'  v-model = 'changeName' /></p>
            <p class="Tips">按&nbsp;<strong>ctrl&nbsp;+&nbsp;enter</strong>&nbsp;接受并关闭面板</p>
        </div>
        <div slot="footer">
            <Button type="text"  @click='modalFlag=false'>取&nbsp;消</Button>
            <Button type="primary"  @click.stop="userExitfun" >编&nbsp;辑</Button>
        </div>
    </Modal>
</template>

<script>
    export default {
        props:['ExitModalFalg','selectNodeName','treeNodeId'],
        data() {
            return {
                 // modal标志位
                modalFlag:false,
                //改变的名称
                changeName:'',
                //名字是否改变标志位
                changeNameFlag:'',
            };
        },
        methods:{
            // 点击 编辑  上传 节点
            userExitfun(){
                this.modalFlag = false;
                if(this.changeNameFlag === this.changeName||this.changeName === '')
                    return;
                let name = this.changeName.replace(/^\s+|\s+$/g,"");
                this.$emit('ExitNameS',14,name);
                // this.patch_json(url,{'name':name}).then(res=>{
                //     this.$emit('ExitNameS',3,res.data.name);
                // }).catch(()=>{})
            },
            //监听 ctrl + ender 按键 执行函数
            upCtrlEnter(e){
                if (e.ctrlKey && e.keyCode == 13){
                    this.userExitfun()
                }
            }
        },
        watch:{
             // 监听 treelist 事件
            ExitModalFalg(){
                this.modalFlag = true;
                this.changeName = this.selectNodeName;
                this.changeNameFlag = this.selectNodeName;
                this.$nextTick(() => {
                    let IputDom = document.querySelector('#modalExitInput .ivu-input');
                    IputDom.focus();
                    IputDom.setSelectionRange(0, this.selectNodeName.length);
                })
            },
            // 监听 modalflag = true
            modalFlag(val){
                if(val){
                    document.addEventListener('keyup',this.upCtrlEnter);
                }else{
                    document.removeEventListener('keyup',this.upCtrlEnter);
                }
            }
        }
    }
</script>

<style  scoped>
    .know-modal-header{
        text-align: center;
    }
    .Tips{
        color: #c5c8ce;
    }
</style>