from django.core.mail import send_mail

class TaskCompletionObserver:
    def update(self, task):
        if task.complete:
            user = task.user
            task.subject = f"Task '{task.title}' has been completed!"
            task.complete=True
            task.message = f"Dear {user.username},\n\nThe task '{task.title}' has been completed.\n\nRegards,\nYour Application"
            send_mail(task.subject, task.message, 'gorongesica@gmail.com', [user.email])
            print(f"Task {task.title} completed. Sending notification...")

class TaskCompletionNotifier:
    def __init__(self):
        self.observers = []

    def attach(self, observer):
        self.observers.append(observer)

    def detach(self, observer):
        self.observers.remove(observer)

    def notify_observers(self, task):
        for observer in self.observers:
            observer.update(task)
