# kotlin-douyu
connect douyu server and print bullet screen data by kotlin


##purpose

Kotlin-douyu aim to receive bullet screen from douyu.tv,and analyze people action by bullet screen data model.

##feature(s)

living rooms
syslog by log4j
ELK (Elasticsearch - Logstash - Kibana) bullet screen data model analyzation


##diagram

###package
  - connector connect to remote bullet screen service
  - data data model package
  - event bullet screen event\heartbeat event and so on
  - listener what to do case a event come
  - logger custom logger by kotlin
  - protocol data exchange from douyu APIs
  - service wrapper of connector and protocol
  - utils common util
