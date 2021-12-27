package com.mukut.demo.controller;

import com.mukut.demo.comparator.*;
import com.mukut.demo.entity.Comment;
import com.mukut.demo.entity.Post;
import com.mukut.demo.entity.Tag;
import com.mukut.demo.entity.User;
import com.mukut.demo.model.AuthorModel;
import com.mukut.demo.model.SortModel;
import com.mukut.demo.model.TagModel;
import com.mukut.demo.repo.CommentRepository;
import com.mukut.demo.repo.PostRepository;
import com.mukut.demo.repo.TagRepository;
import com.mukut.demo.repo.UserRepository;
import com.mukut.demo.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
public class HomeController {

    @Autowired
    public UserRepository userRepository ;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private CommentRepository commentRepository;


    @GetMapping("test/{id}")
    public String test (
            @RequestParam(value = "author", required = false) String author,
            @RequestParam(value = "tag", required = false) String tag,
            @RequestParam(value = "tt", required = false) String v3,
            @PathVariable("id") String id
    ){
        System.out.println(author+"-"+tag+"-"+v3+"-"+id);
        return "redirect:/";
    }


    AuthorModel authorModel = new AuthorModel();
    TagModel tagsModel = new TagModel();
    SortModel sortModel = new SortModel();
    final int START = 1;
    final int LIMIT = 1;

    @GetMapping("/")
    public String getBlogList (
            @RequestParam(value = "search", required = false) String keyword,
            @RequestParam(value = "start", required = false) Integer start,
            @RequestParam(value = "limit", required = false) Integer limit,
            @RequestParam(value = "author", required = false) String author,
            @RequestParam(value = "tag", required = false) String tag,
            @RequestParam(value = "sortField", required = false) String sortField,
            @RequestParam(value = "order", required = false) String order,
            Model model
    ) {
        List<Post> thePosts = null;
        Set<Post> postByKeywordSearch = null;
        Set<Post> postByTags = null; //filter
        Set<Post> postByAuthor = null; //filter
        Set<Post> postByPubDate = null; //filter
        Set<Post> mergedSet = new HashSet<>(); //merge result
        thePosts = new PostService().findAll(postRepository);

        if (keyword!=null && keyword.replaceAll(" ","").length()!=0) {
            postByKeywordSearch = postRepository.findByKeyword(keyword, author, order);
            mergedSet.addAll(searchTag(keyword));
            model.addAttribute("search", keyword);
        }
        if (author!=null && author.length()>1 && (!author.equals("all"))) {
            List<String> authorList = new HelperService().makeListFromCSV(author);
            authorModel.setSwarnava(false);
            authorModel.setGuddu(false);
            authorModel.setKalaiya(false);
            authorModel.setDhritimoy(false);
            for(String authorName: authorList){
                postByAuthor = postRepository.findByAuthor(authorName, sortField, order);
                mergedSet.addAll(postByAuthor);
                if(authorName.equals("swarnava")){
                    authorModel.setSwarnava(true);
                }
                if(authorName.equals("guddu")){
                    authorModel.setGuddu(true);
                }
                if(authorName.equals("kalaiya")){
                    authorModel.setKalaiya(true);
                }
                if(authorName.equals("dhritimoy")){
                    authorModel.setDhritimoy(true);
                }
            }
        }else{
            authorModel.setSwarnava(false);
            authorModel.setGuddu(false);
            authorModel.setKalaiya(false);
            authorModel.setDhritimoy(false);
        }


/*
        if ( (start!=null && limit!=null && start!=limit)  ) {
            mergedSet.addAll(postRepository.pagination(limit, start));
        }else{
            start = 0;
            limit = 10;
        }

 */



        if (tag!=null && tag.length()>1 && (!tag.equals("all"))) {
            List<String> tagList = new HelperService().makeListFromCSV(tag);

            tagsModel.setTechnology(false);
            tagsModel.setLifestyle(false);
            tagsModel.setMotivation(false);
            tagsModel.setPolitics(false);
            tagsModel.setCulture(false);

            for(String tagName: tagList){
                mergedSet.addAll(searchTag(tagName));
                if(tagName.equals("technology")){
                    tagsModel.setTechnology(true);
                }
                if(tagName.equals("lifestyle")){
                    tagsModel.setLifestyle(true);
                }
                if(tagName.equals("motivation")){
                    tagsModel.setMotivation(false);
                }
                if(tagName.equals("politics")){
                    tagsModel.setPolitics(true);
                }
                if(tagName.equals("culture")){
                    tagsModel.setCulture(true);
                }
            }
        }else{
            tagsModel.setTechnology(false);
            tagsModel.setLifestyle(false);
            tagsModel.setMotivation(false);
            tagsModel.setPolitics(false);
            tagsModel.setCulture(false);
        }

        if(postByAuthor!=null){
            mergedSet.addAll(postByAuthor);
        }
        if(postByKeywordSearch!=null){
            mergedSet.addAll(postByKeywordSearch);
        }

        List<Post> convertedList = new ArrayList<>();
        convertedList.addAll(mergedSet);//convert set to list

        if(start == null || limit == null){
            start = 0;
            limit = 10;
        }

        List<Post> sortedList;
        int size;
        if (!convertedList.isEmpty()){
            sortedList = sort(convertedList, sortField, order);
            size = sortedList.size();
            limit = (size<limit)?size:limit;
            model.addAttribute("posts_list", sortedList.subList(start, limit));//mergedSet
        }
        else{
            sortedList = sort(thePosts, sortField, order);
            size = sortedList.size();
            limit = (size<limit)?size:limit;
            model.addAttribute("posts_list", sortedList.subList(start, limit)); ////thePosts
        }
        model.addAttribute("totalSize", size);

        model.addAttribute("authorsModel", authorModel);
        model.addAttribute("tagsModel", tagsModel);
        model.addAttribute("sortModel", sortModel);
        return "post/posts_list";
    }

    public List<Post> sort(List<Post> sortedList, String sortField, String order){
        if(sortField != null){
            sortModel.setAuthor(false);
            sortModel.setExcerpt(false);
            sortModel.setPublishedAt(false);
            sortModel.setTitle(false);
            sortModel.setAsc(false);
            sortModel.setDesc(false);
            if(sortField.equals("author")){
                sortModel.setAuthor(true);
                System.out.println("applying sort based on author");
                if(order!=null && order.equals("desc")) {
                    sortModel.setDesc(true);
                    Collections.sort(sortedList, new PostAuthorComparatorDesc());
                }
                else{
                    sortModel.setAsc(true);
                    Collections.sort(sortedList, new PostAuthorComparatorAsc());
                }
            }
            else if(sortField.equals("title")){
                sortModel.setTitle(true);
                System.out.println("applying sort based on title");
                if(order!=null && order.equals("desc")){
                    sortModel.setDesc(true);
                    Collections.sort(sortedList, new PostTitleComparatorDesc());
                }
                else{
                    sortModel.setAsc(true);
                    Collections.sort(sortedList, new PostTitleComparatorAsc());
                }
            }
            else if(sortField.equals("excerpt")){
                sortModel.setExcerpt(true);
                System.out.println("applying sort based on excerpt");
                if(order!=null && order.equals("desc")){
                    sortModel.setDesc(true);
                    Collections.sort(sortedList, new PostExcerptComparatorDesc());
                }
                else{
                    sortModel.setAsc(true);
                    Collections.sort(sortedList, new PostExcerptComparatorAsc());
                }
            }
            else {
                sortModel.setPublishedAt(true);
                System.out.println("else : applying sort based on pubAt");
                if(order!=null && order.equals("desc")){
                    sortModel.setDesc(true);
                    Collections.sort(sortedList, new PostPublishedAtComparatorDesc());
                }
                else{
                    sortModel.setAsc(true);
                    Collections.sort(sortedList, new PostPublishedAtComparatorAsc());
                }
            }
        } else{
            sortModel.setAuthor(false);
            sortModel.setExcerpt(false);
            sortModel.setPublishedAt(true);
            sortModel.setTitle(false);
            sortModel.setAsc(false);
            sortModel.setDesc(false);

            System.out.println("sortField==null : applying sort based on sortField");
            if(order!=null && order.equals("desc")){
                Collections.sort(sortedList, new PostTitleComparatorDesc());
            }
            else{
                sortModel.setAsc(true);
                Collections.sort(sortedList, new PostTitleComparatorAsc());
            }
        }
        return sortedList;
    }

    public Set<Post> searchTag(String tag){
        List<Tag> tags = tagRepository.findByTag(tag);
        Set<Post> tagSet = new LinkedHashSet<>();
        for (Tag eachTag: tags) {
            Optional<Post> optionalPost = postRepository.findById(eachTag.getPostId());
            if(!optionalPost.isEmpty()){
                if(optionalPost.get()!=null)
                    tagSet.add(optionalPost.get());
            }
        }
        return tagSet;
    }


    @GetMapping("/deleteBlogPost")
    public String deleteBlogPost(
            @RequestParam("postId") int postId
    ) {
        postRepository.deleteById(postId);
        return "redirect:/";
    }

    PostService postService = new PostService();

    @GetMapping("/updateBlogPost")
    public String updateBlogPost(
            @RequestParam("postId") int postId,
            Model model
    ) {
        Post post = postService.findPostById(postRepository ,postId);
        //Post post = postRepository.getById(postId);
        model.addAttribute("post", post);
        return "post/update_post";
    }
    @PostMapping("post_updated")
    public String postUpdate(
            @ModelAttribute Post post
    ){
        new PostService().save(postRepository, post);
        return "redirect:/";
    }

    @GetMapping("/showBlogPost/deleteComment")
    public String deleteComment(
            @RequestParam(value="id", required=false) int id
    ) {
        Optional<Comment> commentResult = commentRepository.findById(id);
        commentRepository.deleteById(id);
        return "redirect:/showBlogPost?postId="+commentResult.get().getPostId();
    }

    @GetMapping("/showBlogPost/updateComment")
    public String updateComment(
            @RequestParam("id") int commentId,
            Model model
    ) {
        Optional<Comment> optional = commentRepository.findById(commentId);
        Comment comment = optional.get();
        model.addAttribute("comment" ,comment);
        return "comment/update_comment";
    }

    @PostMapping("showBlogPost/comment_updated")
    public String commentUpdated(
            @RequestParam(value = "id", required = false) Integer commentId,
            @RequestParam(value = "comment", required = false) String commentMsg
    ) {
        Optional<Comment> optional = commentRepository.findById(commentId);
        Comment comment = optional.get();
        comment.setComment(commentMsg);
        comment.setId(commentId);
        int postId = comment.getPostId();
        Comment commentInserted = new CommentService().update(commentRepository ,comment);
        return "redirect:/showBlogPost?postId="+postId;
    }

    @GetMapping("/newpost")
    public String newpost(){
        return "post/newpost";
    }


    @PostMapping("/newpost_submitted")
    public String newpost_submit(
            @ModelAttribute Post posts,
            @RequestParam("tags") String tags
    ) {
        Post postInserted = new PostService().save(postRepository, posts);
        new TagService().saveEachTag(tagRepository, tags, postInserted.getId(), postInserted.getCreated_at(), postInserted.getUpdated_at());
        return "redirect:/";
    }





    //################################################# for user

    @GetMapping("/register")
    public String index(){
        return "user/register";
    }

    @PostMapping("/register_done")
    public String register(
            @ModelAttribute User user,
            Model model
    ) {
        User userDetails = new UserService().save(userRepository ,user);
        model.addAttribute("message", userDetails.getFname()+" inserted.\n");
        return "welcome";
    }

    //################################################# for retrieve

    @GetMapping("/userslist")
    public String getUserList (
            Model model
    ) {
        List<User> theUsers = new UserService().findAll(userRepository);
        model.addAttribute("users_list", theUsers);
        model.addAttribute("user5", userRepository.findById(5).toString());
        return "user/users_list";
    }

    @GetMapping("/showBlogPost")
    public String showBlogPost(
            @RequestParam("postId") int postId,
            Model postModel,
            Model tagModel,
            Model commentModel
    ) {
        Post post = null;
        try{
            post = new PostService().findPostById(postRepository ,postId);
        }catch (Exception e){
            e.printStackTrace();
        }
        postModel.addAttribute("post", post);

        List<Tag> tagList = new TagService().findByPostId(tagRepository, postId);
        tagModel.addAttribute("tags", tagList);

        List<Comment> commentList = new CommentService().findByPostId(commentRepository, postId);
        commentModel.addAttribute("comments", commentList);

        return "post/show_post";
    }


    @PostMapping("/comments_submitted")
    public String comments(
            @ModelAttribute("comments") Comment comment
    ) {
        Comment comment_inserted = new CommentService().save(commentRepository ,comment);
        return "redirect:/showBlogPost?postId="+comment.getPostId();
    }



//    findByKeyword
//
//    @GetMapping("/comments_submitted")
//    public String comments(
//            @ModelAttribute("comments") Comment comment
//    ) {
//        System.out.println("\n\ngettingFromHTMLForm:\n"+comment+"\n\n");
//        Comment comment_inserted = new CommentService().save(commentRepository ,comment);
//        return "redirect:/showBlogPost?postId="+comment.getPostId();
//    }

}
