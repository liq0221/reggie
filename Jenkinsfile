pipeline{

	 agent any
	 
	 environment {
		 hello = "123456"
	     world = "456789"
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
               sh "echo $hello"
               //未来，凡是需要取变量值的时候，都用双引号
               sh 'echo ${world}'
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
               //git下载来的代码目录下
               sh 'pwd && ls -alh'
               sh 'mvn -v'
               //打包，jar.。默认是从maven中央仓库下载。 jenkins目录+容器目录；-s指定容器内位置
               //只要jenkins迁移，不会对我们产生任何影响
               sh "echo 默认的工作目录：${WS}"
               //每一行指令都是基于当前环境信息。和上下指令无关
               sh 'cd ${WS} && mvn clean package -s "/var/jenkins_home/appconfig/maven/settings.xml"  -Dmaven.test.skip=true '
               //jar包推送给maven repo ，nexus
               //如何让他适用阿里云镜像源

            }
        }
	 }
}
