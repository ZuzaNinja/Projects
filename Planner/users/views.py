from typing import Any
from django.forms.models import BaseModelForm
from django.http import HttpResponse
from django.shortcuts import render, redirect
from django.contrib import messages
from django.contrib.auth.decorators import login_required
from .forms import UserRegisterForm, UserUpdateForm, ProfileUpdateForm
from django.views.generic.list import ListView
from .models import Task, Itinerary
from django.views.generic.edit import CreateView, UpdateView, DeleteView
from django.urls import reverse_lazy
from .observers import TaskCompletionObserver
from django.shortcuts import get_object_or_404
from django.contrib.auth.mixins import LoginRequiredMixin

task_observer = TaskCompletionObserver()

from django.views.generic import ListView

from .models import *
from .utils import Calendar
from datetime import datetime
from django.utils.safestring import mark_safe
from datetime import timedelta
import calendar
from .utils import Calendar  # Adjust the import path based on your project structure
from .forms import EventForm
from datetime import date  # Import the date module


def register(request):
    if request.method == 'POST':
        form = UserRegisterForm(request.POST)
        if form.is_valid():
            form.save()
            username = form.cleaned_data.get('username')
            messages.success(request, f'Your account has been created! You are now able to log in')
            return redirect('login1')
    else:
        form = UserRegisterForm()
    return render(request, 'users/register.html', {'form': form})


@login_required
def profile(request):
    if request.method == 'POST':
        u_form = UserUpdateForm(request.POST, instance=request.user)
        p_form = ProfileUpdateForm(request.POST,
                                   request.FILES,
                                   instance=request.user.profile)
        if u_form.is_valid() and p_form.is_valid():
            u_form.save()
            p_form.save()
            messages.success(request, f'Your account has been updated!')
            return redirect('profile')

    else:
        u_form = UserUpdateForm(instance=request.user)
        p_form = ProfileUpdateForm(instance=request.user.profile)

    context = {
        'u_form': u_form,
        'p_form': p_form
    }

    return render(request, 'users/profile.html', context)

class TaskListView(LoginRequiredMixin, ListView):
    model = Task
    template_name = 'users/task_list.html'  
    context_object_name = 'tasks'
    
    def get_context_data(self, **kwargs):
        context = super().get_context_data(**kwargs)
        context['tasks'] = context['tasks'].filter(user=self.request.user)
        context['itinerary'] = Itinerary.objects.filter(user=self.request.user)
        return context

   
class TaskCreate(LoginRequiredMixin, CreateView):
    model = Task
    fields = ['title','complete']
    success_url = reverse_lazy('users:task_list')
    
    def form_valid(self, form):
        form.instance.user=self.request.user
        return super(TaskCreate,self).form_valid(form)
    
    
class DeleteView(LoginRequiredMixin, DeleteView):
    model = Task
    context_object_name = 'tasks'
    success_url = reverse_lazy('users:task_list')
    
class TaskUpdate(LoginRequiredMixin, UpdateView):
    model = Task
    fields = ['title','complete']
    success_url = reverse_lazy('users:task_list')

class CalendarView(ListView):
    model = Event
    template_name = 'users/calendar.html'

    def get_context_data(self, **kwargs):
        context = super().get_context_data(**kwargs)

        # use today's date for the calendar
        d = get_date(self.request.GET.get('day', None))

        # Instantiate our calendar class with today's year and date
        cal = Calendar(d.year, d.month)

        # Call the formatmonth method, which returns our calendar as a table
        html_cal = cal.formatmonth(withyear=True)
        context['calendar'] = mark_safe(html_cal)
        
        d = get_date(self.request.GET.get('month', None))
        context['prev_month'] = prev_month(d)
        context['next_month'] = next_month(d)
        return context

def prev_month(d):
    first = d.replace(day=1)
    prev_month = first - timedelta(days=1)
    month = 'month=' + str(prev_month.year) + '-' + str(prev_month.month)
    return month

def next_month(d):
    days_in_month = calendar.monthrange(d.year, d.month)[1]
    last = d.replace(day=days_in_month)
    next_month = last + timedelta(days=1)
    month = 'month=' + str(next_month.year) + '-' + str(next_month.month)
    return month

def get_date(req_day):
    if req_day:
        year, month = (int(x) for x in req_day.split('-'))
        return date(year, month, day=1)
    return datetime.today()


def event(request, event_id=None):
    instance = Event()
    if event_id:
        instance = get_object_or_404(Event, pk=event_id)
    else:
        instance = Event()
    
    form = EventForm(request.POST or None, instance=instance)
    if request.POST and form.is_valid():
        form.save()
        return HttpResponseRedirect(reverse('users:calendar'))
    return render(request, 'users/event.html', {'form': form})

    def form_valid(self, form):
        if 'complete' in self.request.POST:
            task = form.save(commit=False)
            task.complete = True
            task.save()
        else:
            form.save()
        return super().form_valid(form)
    
class ItineraryListView(LoginRequiredMixin, ListView):
    model = Itinerary
    template_name = 'users/task_list.html'  
    context_object_name = 'itinerary'
    
    def get_context_data(self, **kwargs):
        context= super().get_context_data(**kwargs)
        context['itinerary']=context['itinerary'].filter(user=self.request.user)
        return context


class ItineraryCreate(LoginRequiredMixin, CreateView):
    model = Itinerary
    fields = ['title']
    success_url = reverse_lazy('users:itinerary_list')
    
    def form_valid(self, form):
        form.instance.user=self.request.user
        return super(ItineraryCreate,self).form_valid(form)
    
    
class ItineraryUpdate(LoginRequiredMixin, UpdateView):
    model = Itinerary
    fields = ['title']
    success_url = reverse_lazy('users:itinerary_list')

