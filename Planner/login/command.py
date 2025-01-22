# commands.py
from .models import Post
from abc import ABC, abstractmethod

class Command(ABC):
    @abstractmethod
    def execute(self):
        pass

class CreatePostCommand(Command):
    def __init__(self, author, title, content):
        self.author = author
        self.title = title
        self.content = content

    def execute(self):
        return Post.objects.create(author=self.author, title=self.title, content=self.content)

class UpdatePostCommand(Command):
    def __init__(self, post, title, content):
        self.post = post
        self.title = title
        self.content = content

    def execute(self):
        self.post.title = self.title
        self.post.content = self.content
        self.post.save()
        return self.post
