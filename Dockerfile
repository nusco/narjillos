FROM java
MAINTAINER Paolo "Nusco" Perrotta <paolo.nusco.perrotta@gmail.com>

RUN wget https://github.com/nusco/narjillos/releases/download/v0.8.1/narjillos.zip -O narjillos.zip && unzip -o narjillos.zip && rm narjillos.zip
WORKDIR narjillos
CMD sh narjillos -f -s
