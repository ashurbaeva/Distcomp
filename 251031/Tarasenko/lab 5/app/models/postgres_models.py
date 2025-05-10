from sqlalchemy import Column, String, Integer, ForeignKey
from sqlalchemy.ext.declarative import declarative_base

Base = declarative_base()

class Creator(Base):
    __tablename__ = 'tbl_creator'
    id = Column(Integer, primary_key=True)
    name = Column(String)

class Tweet(Base):
    __tablename__ = 'tbl_tweet'
    id = Column(Integer, primary_key=True)
    content = Column(String)
    creator_id = Column(Integer, ForeignKey('tbl_creator.id'))

class Label(Base):
    __tablename__ = 'tbl_label'
    id = Column(Integer, primary_key=True)
    name = Column(String)
