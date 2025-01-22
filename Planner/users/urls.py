from django.urls import path, include
from .views import TaskListView, TaskCreate, TaskUpdate, DeleteView
from .views import ItineraryListView, ItineraryCreate, ItineraryUpdate
from . import views
from django.urls import re_path

app_name = 'users'
urlpatterns = [
     path('task_list/', TaskListView.as_view(), name='task_list'),
     path('task-create/', TaskCreate.as_view(), name='task-create'),
     path('task-update/<int:pk>', TaskUpdate.as_view(), name='task-update'),
     path('task-delete/<int:pk>', DeleteView.as_view(), name='task-delete'),
     path('task_list/', ItineraryListView.as_view(), name='itinerary_list'),
     path('itinerary-create/', ItineraryCreate.as_view(), name='itinerary-create'),
     path('itinerary-update/', ItineraryUpdate.as_view(), name='itinerary-update'),
   
     path('calendar/', views.CalendarView.as_view(), name='calendar'),
     path('event/new/', views.event, name='event_new'),
      re_path(r'^event/edit/(?P<event_id>\d+)/$', views.event, name='event_edit'),
]
