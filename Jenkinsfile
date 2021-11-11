pipeline{

	 agent any
	 
	 environment {
		 WS="${WORKSPACE}"
		 ALIYUN_SECRTE=credentials("aliyun-docker-repo")
		 SERVER=credentials("ServiceServer")
		 ALIYUN_USER=credentials("aliyun-account")
	 }
	 
	 stages {
		stage('环境检查'){
			steps {
               sh 'printenv'
               echo "正在检测基本信息......."
               sh 'java -version'
               sh 'git --version'
               sh 'docker version'
               sh 'pwd && ls -alh'
               sh "echo end......"
            }
		}
		
		stage('maven编译'){
            //jenkins不配置任何环境的情况下， 仅适用docker 兼容所有场景
            agent {
                docker {
                    image 'maven:3-alpine'
                    args '-v /var/jenkins_home/appconfig/maven/.m2:/root/.m2'
                 }
            }
            steps {
               sh 'pwd && ls -alh'
               sh 'mvn -v'
               sh "echo 默认的工作目录：${WS}"
               sh 'cd ${WS} && mvn clean package -s "/var/jenkins_home/appconfig/maven/settings.xml"  -Dmaven.test.skip=true '

            }
        }
		
		stage('生成镜像'){
            steps {
                echo "打包..."
                //检查Jenkins的docker命令是否能运行
                sh 'docker version'
                sh 'pwd && ls -alh'
                sh 'docker build -t reggie .'

                //镜像就可以进行保存


            }
        }
		
		stage('推送镜像'){
		
             input {
                 message "需要推送远程镜像吗?"
                 ok "需要"
                 parameters {
                     string(name: 'APP_VER', defaultValue: 'v1.0', description: '需要推送的版本')
                 }
             }


             steps {

                echo "$APP_VER"
				
                script {
                        withCredentials([usernamePassword(credentialsId: 'aliyun-docker-repo', passwordVariable: 'ali_pwd', usernameVariable: 'ali_user')]) {
                            // some block
                             sh "docker login -u ${ali_user} -p ${ali_pwd}   registry.cn-hangzhou.aliyuncs.com"
                             sh "docker tag reggie registry.cn-hangzhou.aliyuncs.com/leeq/reggie:${APP_VER}"
                        }
                }
				sh "docker push registry.cn-hangzhou.aliyuncs.com/leeq/reggie:${APP_VER}"

             }
         }
		 
		  stage('部署镜像'){
		  
			input {
                 message "需要部署镜像的版本?"
                 ok "需要"
                 parameters {
                     string(name: 'PUSH_APP_VER', defaultValue: 'v1.0', description: '需要部署镜像的版本')
                 }
             }
            steps {  
					
				  echo "部署镜像..."
				  
				  script {
					  def sshServer = getServer()
					  withCredentials([usernamePassword(credentialsId: 'aliyun-account', passwordVariable: 'password', usernameVariable: 'userName')]) {
						sshCommand remote: sshServer, command: "./deploy-push-image.sh ${PUSH_APP_VER} reggie-${PUSH_APP_VER} reggie ${userName} ${password}"
					  }
				  }
            }
		}
		
	 }
}

def getServer(){
	def remote = [:]
	remote.name = "server"
	remote.host = "152.136.16.45"
	remote.port = 22
	remote.allowAnyHosts = true
	withCredentials([usernamePassword(credentialsId: 'ServiceServer', passwordVariable: 'password', usernameVariable: 'userName')]) {
		remote.user = "${userName}"
		remote.password = "${password}"
	}
	return remote
}
