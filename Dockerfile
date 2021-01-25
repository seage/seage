FROM maven:3-openjdk-11

WORKDIR /seage

COPY . .

RUN /bin/sh ./target/build.sh

CMD tail -f /dev/null

