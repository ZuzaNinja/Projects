from django.db import models
from django.contrib.auth.models import User
from PIL import Image
from .observers import TaskCompletionNotifier
from .observers import TaskCompletionObserver
from django.db.models.signals import post_save
from django.dispatch import receiver
#from .models import Task



class Profile(models.Model):
    user = models.OneToOneField(User, on_delete=models.CASCADE)
    image = models.ImageField(default='default.jpeg', upload_to='profile_pics')

    def __str__(self):
        return f'{self.user.username} Profile'

    def save(self, *args, **kwargs):
        super().save(*args, **kwargs)

        img = Image.open(self.image.path)

        if img.height > 300 or img.width > 300:
            output_size = (300, 300)
            img.thumbnail(output_size)
            img.save(self.image.path)


from django.db import models
from .observers import TaskCompletionNotifier, TaskCompletionObserver

class Task(models.Model):
    user = models.ForeignKey(User, on_delete=models.CASCADE, null=True, blank=True) 
    title = models.CharField(max_length=200) 
    complete = models.BooleanField(default=False)
    create = models.DateTimeField(auto_now_add=True)

    def __str__(self):
        return self.title
    
    class Meta:
        ordering = ['complete']

class Event(models.Model):
    title = models.CharField(max_length=200)
    description = models.TextField()
    start_time = models.DateTimeField()
    end_time = models.DateTimeField()  
    @property
    def get_html_url(self):
        url = reverse('users:event_edit', args=(self.id,))
        return f'<a href="{url}"> {self.title} </a>'      
    
    def save(self, *args, **kwargs):
        super().save(*args, **kwargs)
        
        if self.complete:
            notifier = TaskCompletionNotifier()
            notifier.attach(TaskCompletionObserver()) 
            notifier.notify_observers(self)


class Itinerary(models.Model):
    user = models.ForeignKey(User, on_delete=models.CASCADE, null=True, blank=True) 
    title = models.CharField(max_length=200) 

    def __str__(self):
        return self.title
    
    def save(self, *args, **kwargs):
        super().save(*args, **kwargs)