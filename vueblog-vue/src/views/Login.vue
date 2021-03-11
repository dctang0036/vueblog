<template>
    <div>
        <el-container>
            <el-header>
                <!--<router-link to="/blogs">
                    <img src="https://www.markerhub.com/dist/images/logo/markerhub-logo.png"
                        style="height: 60%; margin-top: 10px;">
                </router-link>-->
                <img class="mlogo" src="https://www.markerhub.com/dist/images/logo/markerhub-logo.png" alt="">
            </el-header>
            <el-main>
                <el-form :model="ruleForm" :rules="rules" ref="ruleForm" label-width="100px"
                         class="demo-ruleForm">
                    <el-form-item label="用户名" prop="userName">
                        <el-input type="text" maxlength="12" v-model="ruleForm.userName"></el-input>
                    </el-form-item>
                    <el-form-item label="密码" prop="password">
                        <el-input type="password" v-model="ruleForm.password" autocomplete="off"></el-input>
                    </el-form-item>
                    <el-form-item>
                        <el-button type="primary" @click="submitForm('ruleForm')">登录</el-button>
                        <el-button @click="resetForm('ruleForm')">重置</el-button>
                    </el-form-item>
                </el-form>
            </el-main>
        </el-container>
    </div>
</template>

<script>
    export default {
        name: "Login",
        data() {
            /*var validataPass = (rule, value, callback) => {
                if (value === '') {
                    callback(new Error('请输入密码'));
                } else {
                    callback();
                }
            };*/
            return {
                ruleForm: {
                    password: '111111',
                    userName: 'markerhub'
                },
                rules: {
                    password: [
                        { required: true, message: '请选择密码', trigger: 'change' }
                    ],
                    userName: [
                        {required: true, message: '请输入用户名', trigger: 'blur'},
                        {min: 3, max: 12, message: '长度在 3 到 12 个字符', trigger: 'blur'}
                    ]
                }
            };
        },
        methods: {
            submitForm(ruleForm) {

                this.$refs[ruleForm].validate((valid) => {
                    if(valid) {
                        // 提交逻辑
                        const _this = this;
                        this.$axios.post('/sys/login', this.ruleForm)
                        .then((res) => {
                            // const token = res.headers['authorization'];
                            const token = res.data.result.token;
                            const userInfo = res.data.result.userInfo;
                            _this.$store.commit('SET_TOKEN', token);
                            _this.$store.commit('SET_USERINFO', userInfo);
                            _this.$router.push("/blogs");
                        })
                    } else {
                        console.log('error submit!!');
                        return false;
                    }
                });
            },
            resetForm(formName) {
                this.$refs[formName].resetFields();
            }
        }
    }
</script>

<style scoped>
    .el-header, .el-footer {
        background-color: #B3C0D1;
        color: #333;
        text-align: center;
        line-height: 60px;
    }
    .el-aside {
        background-color: #D3DCE6;
        color: #333;
        text-align: center;
        line-height: 200px;
    }
    .el-main {
        /*background-color: #E9EEF3;*/
        color: #333;
        text-align: center;
        line-height: 160px;
    }
    body > .el-container {
        margin-bottom: 40px;
    }
    .el-container:nth-child(5) .el-aside,
    .el-container:nth-child(6) .el-aside {
        line-height: 260px;
    }
    .el-container:nth-child(7) .el-aside {
        line-height: 320px;
    }
    .mlogo {
        height: 60%;
        margin-top: 10px;
    }
    .demo-ruleForm {
        max-width: 500px;
        margin: 0 auto;
    }
</style>
