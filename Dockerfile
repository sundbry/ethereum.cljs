# Docker for ethereum.cljs test suite
# Requires Ethereum RPC on $ETH_HOST:8545
FROM debian

MAINTAINER ryan.sundberg@gmail.com

# ethereum.cljs git tag
ENV GIT_TAG="master" 
# Ethereum RPC host
# ENV ETH_HOST "localhost"

EXPOSE 8081

# System setup
RUN apt-get update -q -q
RUN apt-get install -y -q -q git wget
RUN apt-get install -y -q -q openjdk-7-jre

# Share lein 
ENV LEIN_HOME=/usr/local/share/lein

# Install lein
RUN wget https://raw.githubusercontent.com/technomancy/leiningen/stable/bin/lein -O /usr/local/bin/lein
RUN chmod 0755 /usr/local/bin/lein
RUN LEIN_ROOT=true /usr/local/bin/lein version

# Install app
RUN useradd -m app
WORKDIR /home/app
USER app

RUN git clone https://github.com/sundbry/ethereum.cljs.git -b $GIT_TAG
WORKDIR /home/app/ethereum.cljs
RUN git branch
RUN /usr/local/bin/lein do deps, cljsbuild once

ENTRYPOINT ["/usr/local/bin/lein", "run"]
