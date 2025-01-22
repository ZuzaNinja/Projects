from django.shortcuts import render, redirect
from django.contrib.auth.mixins import LoginRequiredMixin, UserPassesTestMixin
from django.views.generic import (
    ListView,
    DetailView,
    CreateView,
    UpdateView,
    DeleteView
)
from django.contrib.auth.decorators import login_required
from django.contrib import messages

from .models import Post, Comment
from django.shortcuts import get_object_or_404

from django.urls import reverse
from django.urls import reverse_lazy
def home(request):
    context = {
    }
    return render(request, 'login/home.html', context)


def destinations(request):
    context = {
    }
    return render(request, 'login/destinations.html', context)


def about(request):
    context = {
        
    }
    return render(request, 'login/about.html', context)



def post(request):
    posts_with_likes = Post.objects.annotate(like_count=Count('likes'))

    context = {
        'posts': Post.objects.all()
        
    }
    return render(request, 'login/post.html', context)
    


class PostListView(ListView):
    model = Post
    template_name = 'login/post.html'  # <app>/<model>_<viewtype>.html
    context_object_name = 'posts'
    ordering = ['-date_posted']
    def get_queryset(self):
        return Post.objects.all()  


class PostDetailView(DetailView):
    model = Post

    def get_context_data(self, **kwargs):
        context = super().get_context_data(**kwargs)
        post = self.get_object()
        context['like_count'] = post.likes.count()
        context['comments'] = Comment.objects.filter(post=post)
        return context
    

# Add a new view for handling comments
class CommentCreateView(LoginRequiredMixin, CreateView):
    model = Comment
    fields = ['content']

    def form_valid(self, form):
        form.instance.author = self.request.user
        form.instance.post = Post.objects.get(pk=self.kwargs['pk'])
        return super().form_valid(form)

    def get_success_url(self):
        # Redirect to the post detail page after successful form submission
        return reverse('post-detail', kwargs={'pk': self.kwargs['pk']})
# Modify your like_post view to include redirecting back to the post detail page
def like_post(request, pk):
    post = get_object_or_404(Post, pk=pk)
    if request.user in post.likes.all():
        post.likes.remove(request.user)
    else:
        post.likes.add(request.user)
    return redirect('post-detail', pk=pk)

class CommentUpdateView(LoginRequiredMixin, UserPassesTestMixin, UpdateView):
    model = Comment
    fields = ['content']
    template_name = 'login/comment_form.html'  # Create a new template for comment editing

    def test_func(self):
        comment = self.get_object()
        return self.request.user == comment.author

    def get_success_url(self):
        return reverse_lazy('post-detail', kwargs={'pk': self.object.post.pk})


@login_required
def comment_delete(request, pk):
    comment = get_object_or_404(Comment, pk=pk)
    
    # Check if the user has permission to delete the comment
    if request.user == comment.author:
        comment.delete()
        messages.success(request, 'Comment deleted successfully.')
    else:
        messages.error(request, 'You do not have permission to delete this comment.')

    return redirect('post-detail', pk=comment.post.pk)
class PostCreateView(LoginRequiredMixin, CreateView):
    model = Post
    fields = ['title', 'content']

    def form_valid(self, form):
        form.instance.author = self.request.user
        return super().form_valid(form)


class PostUpdateView(LoginRequiredMixin, UserPassesTestMixin, UpdateView):
    model = Post
    fields = ['title', 'content']

    def form_valid(self, form):
        form.instance.author = self.request.user
        return super().form_valid(form)

    def test_func(self):
        post = self.get_object()
        if self.request.user == post.author:
            return True
        return False


class PostDeleteView(LoginRequiredMixin, UserPassesTestMixin, DeleteView):
    model = Post
    success_url = '/'

    def test_func(self):
        post = self.get_object()
        if self.request.user == post.author:
            return True
        return False


def about(request):
    return render(request, 'login/about.html', {'title': 'About'})

@login_required
def like_post(request, pk):
    post = get_object_or_404(Post, pk=pk)

    # Assuming the ForeignKey to CustomUser is named 'author' in your Post model
    if request.user == post.author:
        messages.error(request, 'You cannot like your own post.')
    else:
        # Check if the user has already liked the post
        if request.user in post.likes.all():
            post.likes.remove(request.user)
        else:
            post.likes.add(request.user)

    return redirect('post-detail', pk=pk)