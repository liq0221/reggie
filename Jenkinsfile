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
	 }
}
