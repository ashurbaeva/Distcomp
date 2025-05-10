from cassandra.cluster import Cluster
from cassandra.query import dict_factory
from uuid import UUID

cluster = Cluster(['cassandra'])
session = cluster.connect('publisher')
session.row_factory = dict_factory

def get_post_by_id_from_cassandra(post_id: UUID):
    row = session.execute("SELECT * FROM tbl_post WHERE id = %s", (post_id,))
    return row.one()
