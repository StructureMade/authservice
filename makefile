dev:
	mvn clean
	mvn install -DskipTests=true
	docker build -f Dockerfile -t authservice .
	docker-compose up
prod:
	mvn clean
	mvn install -DskipTests=true
