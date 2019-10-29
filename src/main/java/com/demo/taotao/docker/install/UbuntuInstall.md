## office ubuntu install process

### first step
1.卸载旧版 
    sudo apt-get remove docker docker-engine docker.io containerd runc

2.更新apt包索引 
    sudo apt-get update

3.安装 apt 依赖包，用于通过HTTPS来获取仓库:
    sudo apt-get install \
    apt-transport-https \
    ca-certificates \
    curl \
    gnupg-agent \
    software-properties-common

4.添加 Docker 的官方 GPG 密钥：
 curl -fsSL https://download.docker.com/linux/ubuntu/gpg | sudo apt-key add -
 国内镜像路径
 curl -sSL http://acs-public-mirror.oss-cn-hangzhou.aliyuncs.com/docker-engine/internet | sh -
 
5. 通过搜索指纹的后8个字符，验证您现在是否拥有带有指纹的密钥。
    sudo apt-key fingerprint 0EBFCD88
    pub   rsa4096 2017-02-22 [SCEA]
          9DC8 5822 9FC7 DD38 854A  E2D8 8D81 803C 0EBF CD88
    uid           [ unknown] Docker Release (CE deb) <docker@docker.com>
    sub   rsa4096 2017-02-22 [S] 

6.使用以下指令设置稳定版仓库
    sudo add-apt-repository \
       "deb [arch=amd64] https://download.docker.com/linux/ubuntu \
      $(lsb_release -cs) \
      stable"
 
 
7.安装 Docker Engine-Community
    sudo apt-get update 

8.安装最新版本的 Docker Engine-Community 和 containerd ，或者转到下一步安装特定版本：
    sudo apt-get install docker-ce docker-ce-cli containerd.io

8.1 要安装特定版本的 Docker Engine-Community，请在仓库中列出可用版本，然后选择一种安装。列出您的仓库中可用的版本：
 apt-cache madison docker-ce

  docker-ce | 5:18.09.1~3-0~ubuntu-xenial | https://download.docker.com/linux/ubuntu  xenial/stable amd64 Packages
  docker-ce | 5:18.09.0~3-0~ubuntu-xenial | https://download.docker.com/linux/ubuntu  xenial/stable amd64 Packages
  docker-ce | 18.06.1~ce~3-0~ubuntu       | https://download.docker.com/linux/ubuntu  xenial/stable amd64 Packages
  docker-ce | 18.06.0~ce~3-0~ubuntu       | https://download.docker.com/linux/ubuntu  xenial/stable amd64 Packages
  ...
8.2 使用第二列中的版本字符串安装特定版本，例如 5:18.09.1~3-0~ubuntu-xenial。
sudo apt-get install docker-ce=<VERSION_STRING> docker-ce-cli=<VERSION_STRING> containerd.io

9.测试 Docker 是否安装成功，输入以下指令，打印出以下信息则安装成功:
    sudo docker run hello-world
    


### user1 install process  Ubuntu 16.04 安装 Docker

1.选择国内的云服务商，这里选择阿里云为例
curl -sSL http://acs-public-mirror.oss-cn-hangzhou.aliyuncs.com/docker-engine/internet | sh -
2.安装所需要的包

sudo apt-get install linux-image-extra-$(uname -r) linux-image-extra-virtual
3.添加使用 HTTPS 传输的软件包以及 CA 证书

sudo apt-get update
sudo apt-get install apt-transport-https ca-certificates
4.添加GPG密钥

sudo apt-key adv --keyserver hkp://p80.pool.sks-keyservers.net:80 --recv-keys 58118E89F3A912897C070ADBF76221572C52609D
5.添加软件源

echo "deb https://apt.dockerproject.org/repo ubuntu-xenial main" | sudo tee /etc/apt/sources.list.d/docker.list
6.添加成功后更新软件包缓存

sudo apt-get update
7.安装docker

sudo apt-get install docker-engine
8.启动 docker

sudo systemctl enable docker
sudo systemctl start docker


### user2 install process  Ubuntu 18.04 安装 Docker-ce

1.更换国内软件源，推荐中国科技大学的源，稳定速度快（可选）
****
sudo cp /etc/apt/sources.list /etc/apt/sources.list.bak
sudo sed -i 's/archive.ubuntu.com/mirrors.ustc.edu.cn/g' /etc/apt/sources.list
sudo apt update
2.安装需要的包

sudo apt install apt-transport-https ca-certificates software-properties-common curl
3.添加 GPG 密钥，并添加 Docker-ce 软件源，这里还是以中国科技大学的 Docker-ce 源为例

curl -fsSL https://mirrors.ustc.edu.cn/docker-ce/linux/ubuntu/gpg | sudo apt-key add -
sudo add-apt-repository "deb [arch=amd64] https://mirrors.ustc.edu.cn/docker-ce/linux/ubuntu \
$(lsb_release -cs) stable"
4.添加成功后更新软件包缓存

sudo apt update
5.安装 Docker-ce

sudo apt install docker-ce
6.设置开机自启动并启动 Docker-ce（安装成功后默认已设置并启动，可忽略）

sudo systemctl enable docker
sudo systemctl start docker
7.测试运行

sudo docker run hello-world
8.添加当前用户到 docker 用户组，可以不用 sudo 运行 docker（可选）

sudo groupadd docker
sudo usermod -aG docker $USER
9.测试添加用户组（可选）

docker run hello-world

