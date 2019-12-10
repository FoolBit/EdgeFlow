## 如何安装Face Recognition

### 安装dlib

**安装依赖**

```shell
apt-get install -y --fix-missing build-essential cmake gfortran git wget curl graphicsmagick libgraphicsmagick1-dev libatlas-dev libavcodec-dev libavformat-dev libgtk2.0-dev libjpeg-dev liblapack-dev libswscale-dev pkg-config python3-dev python3-numpy software-properties-common zip
```

安装`dlib`

``` shell
git clone https://github.com/davisking/dlib.git

cd dlib
mkdir build; cd build; cmake ..; cmake --build .

cd ..
python3 setup.py install
```

### 安装face recognition

```shell
pip install face_recognition
```



搞定！