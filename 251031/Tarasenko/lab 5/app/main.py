from fastapi import FastAPI
from app.api import post_controller

app = FastAPI()
app.include_router(post_controller.router)
