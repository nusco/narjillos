FROM java
MAINTAINER Paolo "Nusco" Perrotta <paolo.nusco.perrotta@gmail.com>

RUN wget https://github.com/nusco/narjillos/releases/download/v0.8.1/narjillos-0.8.1.zip -O narjillos.zip && unzip -o narjillos.zip && rm narjillos.zip
WORKDIR narjillos-0.8.1
CMD sh narjillos -f -s

