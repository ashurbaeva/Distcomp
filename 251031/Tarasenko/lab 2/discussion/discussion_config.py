from cassandra.cluster import Cluster

def get_cassandra_session():
    cluster = Cluster(["localhost"])  # Подключаемся к Cassandra
    session = cluster.connect()
    session.set_keyspace("distcomp")  # Используем схему distcomp
    return session

cassandra_session = get_cassandra_session()
