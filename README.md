
### Goal
Build a simple JavaFX GUI application that calculates the total cost of items in a shopping cart, supports localization (multiple languages), and demonstrates Docker and CI/CD pipeline integration### Build Jenkins(CICD)-push docker  
### Testing Docker Container (Windows + VcXsrv)
1. Before running the container, start VcXsrv/XLaunch with:

✔ Clipboard

✔ Primary Selection

❌ Native OpenGL (must be OFF)

✔ Disable access control  
2. Start docker destop
3. Run with Docker Destop
or terminal run
```bash
docker run -it thanh0201/javashoppingcartwithlocalizationui:latest
````
### Run Minikube with Docker Desktop(backend)
1. Start minikube
Open terminal(command prompt)
```bash
minikube start --driver=docker
```
2. build the image
```bash
docker build -t thanh0201/javashoppingcartwithlocalizationui:latest .
```
3. Deploy to the kubernetes
```bash
kubectl apply -f deployment.yaml
kubectl get pods
```
