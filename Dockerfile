FROM openjdk:17-jdk-slim

WORKDIR /seage

COPY . .

RUN ./scripts/build.sh

CMD tail -f /dev/null

