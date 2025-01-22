from django.db import models
from django.utils import timezone
from django.contrib.auth.models import User
from django.urls import reverse


    
class CustomUser(models.Model):
    username = models.CharField(max_length=50, unique=True)
    email = models.EmailField(unique=True)
    password = models.CharField(max_length=100)
    first_name = models.CharField(max_length=45)
    last_name = models.CharField(max_length=45)


    USERNAME_FIELD = 'email'
    REQUIRED_FIELDS = ['username']

    def __str__(self):
        return self.username

class Comment(models.Model):
    post = models.ForeignKey('Post', on_delete=models.CASCADE)
    author = models.ForeignKey(User, on_delete=models.CASCADE)
    content = models.TextField()
    date_posted = models.DateTimeField(default=timezone.now)

    def __str__(self):
        return f'{self.author.username} - {self.post.title}'
class Post(models.Model):
    title = models.CharField(max_length=100)
    content = models.TextField()
    date_posted = models.DateTimeField(default=timezone.now)
    author = models.ForeignKey(User, on_delete=models.CASCADE)
    likes = models.ManyToManyField(User, related_name='post_likes', blank=True)
    comments = models.ManyToManyField(Comment, related_name='post_comments', blank=True)

    def __str__(self):
        return self.title

    def get_absolute_url(self):
        return reverse('post-detail', kwargs={'pk': self.pk})