package com.mukut.demo.controller;

import com.mukut.demo.comparator.*;
import com.mukut.demo.entity.Comment;
import com.mukut.demo.entity.Post;
import com.mukut.demo.entity.Tag;
import com.mukut.demo.entity.User;
import com.mukut.demo.model.AuthorModel;
import com.mukut.demo.model.PageModel;
import com.mukut.demo.model.SortModel;
import com.mukut.demo.model.TagModel;
import com.mukut.demo.repo.CommentRepository;
import com.mukut.demo.repo.PostRepository;
import com.mukut.demo.repo.TagRepository;
import com.mukut.demo.repo.UserRepository;
import com.mukut.demo.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
public class HomeController {

    @Autowired
    public UserRepository userRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private CommentRepository commentRepository;


    @GetMapping("test/")
    public String test(
            @RequestParam(value = "author", required = false) String author,
            @RequestParam(value = "start", required = false) Integer start,
            @RequestParam(value = "limit", required = false) Integer limit,
            Model model
    ) {
        Page<Post> page = postService.findPaginated(postRepository, 1, 10);
        List<Post> pageContent = page.getContent();

        System.out.println(pageContent);
        model.addAttribute("posts_list", pageContent); ////thePosts
//
//
//        model.addAttribute("currentPage", 1);
//        model.addAttribute("totalPages", page.getTotalPages());
//        model.addAttribute("totalItems", page.getTotalElements());
//
//        model.addAttribute("sortField", "asc");
//        model.addAttribute("sortDir", "asc");
       // model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");

        //model.addAttribute("listEmployees", pageContent);

        return "post/posts_list";
        //return "redirect:/";
    }


    AuthorModel authorModel = new AuthorModel();
    TagModel tagsModel = new TagModel();
    SortModel sortModel = new SortModel();
    final int START = 1;


    @GetMapping("/")
    public String getBlogList(
            @RequestParam(value = "search", required = false) String keyword,
            @RequestParam(value = "start", required = false) Integer start,
            @RequestParam(value = "limit", required = false) Integer limit,
            @RequestParam(value = "author", required = false) String author,
            @RequestParam(value = "tag", required = false) String tag,
            @RequestParam(value = "sortField", required = false) String sortField,
            @RequestParam(value = "order", required = false) String order, Model model) {
        List<Post> thePosts = null;
        Set<Post> postByKeywordSearch = null;
        Set<Post> postByTags = null; //filter
        Set<Post> postByAuthor = null; //filter
        Set<Post> postByPubDate = null; //filter
        Set<Post> mergedSet = new HashSet<>(); //merge result
        thePosts = new PostService().findAll(postRepository);

        if (keyword != null && keyword.replaceAll(" ", "").length() != 0) {
            postByKeywordSearch = postRepository.findByKeyword(keyword);
            mergedSet.addAll(searchTag(keyword));
            model.addAttribute("search", keyword);
        }
        if (author != null && author.length() > 1 && (!author.equals("all"))) {
            List<String> authorList = new HelperService().makeListFromCSV(author);
            authorModel.setSwarnava(false);
            authorModel.setGuddu(false);
            authorModel.setKalaiya(false);
            authorModel.setDhritimoy(false);
            for (String authorName : authorList) {
                postByAuthor = postRepository.findByAuthor(authorName);
                mergedSet.addAll(postByAuthor);
                if (authorName.equals("swarnava")) {
                    authorModel.setSwarnava(true);
                }
                if (authorName.equals("guddu")) {
                    authorModel.setGuddu(true);
                }
                if (authorName.equals("kalaiya")) {
                    authorModel.setKalaiya(true);
                }
                if (authorName.equals("dhritimoy")) {
                    authorModel.setDhritimoy(true);
                }
            }
        } else {
            authorModel.setSwarnava(false);
            authorModel.setGuddu(false);
            authorModel.setKalaiya(false);
            authorModel.setDhritimoy(false);
        }


        if (tag != null && tag.length() > 1 && (!tag.equals("all"))) {
            List<String> tagList = new HelperService().makeListFromCSV(tag);

            tagsModel.setTechnology(false);
            tagsModel.setLifestyle(false);
            tagsModel.setMotivation(false);
            tagsModel.setPolitics(false);
            tagsModel.setCulture(false);

            for (String tagName : tagList) {
                mergedSet.addAll(searchTag(tagName));
                if (tagName.equals("technology")) {
                    tagsModel.setTechnology(true);
                }
                if (tagName.equals("lifestyle")) {
                    tagsModel.setLifestyle(true);
                }
                if (tagName.equals("motivation")) {
                    tagsModel.setMotivation(false);
                }
                if (tagName.equals("politics")) {
                    tagsModel.setPolitics(true);
                }
                if (tagName.equals("culture")) {
                    tagsModel.setCulture(true);
                }
            }
        } else {
            tagsModel.setTechnology(false);
            tagsModel.setLifestyle(false);
            tagsModel.setMotivation(false);
            tagsModel.setPolitics(false);
            tagsModel.setCulture(false);
        }

        if (postByAuthor != null) {
            mergedSet.addAll(postByAuthor);
        }
        if (postByKeywordSearch != null) {
            mergedSet.addAll(postByKeywordSearch);
        }

        List<Post> convertedList = new ArrayList<>();
        convertedList.addAll(mergedSet); //convert set to list

        //Pagination
        int size ;
        if (start == null) {
            start = 0;
        }
        if (limit == null) {
            limit = LIMIT;
        }

        List<PageModel> paginationUrl;
        List<Post> sortedList;
        if (!convertedList.isEmpty()) {
            System.out.println("!convertedList.isEmpty()");
            sortedList = sort(convertedList, sortField, order);
            size = sortedList.size();
            //limit += start;



            paginationUrl = pagination(sortedList, start, limit);

            //limit = (size < limit) ? size : limit;

            model.addAttribute("posts_list", createSubList(sortedList, start, limit)); //mergedSet
        } else {
            System.out.println("convertedList.isEmpty()");
            sortedList = sort(thePosts, sortField, order);
            size = sortedList.size();

            System.out.println("start:" + start + " ,  limit: " + limit);
            //limit += start;
            //limit = (size < limit) ? size : limit;
            System.out.println("start:" + start + " ,  limit: " + limit);
            System.out.println("sortedList\n" + sortedList);
            System.out.println("sortedList.subList\n" + sortedList.subList(start, start + limit));


            paginationUrl = pagination(sortedList, start, limit);

            model.addAttribute("posts_list", createSubList(sortedList, start, limit)); ////thePosts
        }



        model.addAttribute("totalSize", size);
        model.addAttribute("authorsModel", authorModel);
        model.addAttribute("tagsModel", tagsModel);
        model.addAttribute("sortModel", sortModel);
        model.addAttribute("pagination_url", paginationUrl);


        //test
        model.addAttribute("currentPage", 1);
        model.addAttribute("totalPage", (size/LIMIT));//modify later: case:only 5 pg, case:left 1 page

        return "post/posts_list";
    }



    public List<Post> createSubList(List<Post> post, int start, int limit){
        List<Integer> list = new ArrayList<>();

        int fromIndex=start, toIndex=limit, size = post.size();
        if (start >= size) {
            System.out.println(" start >= size ");
            fromIndex = size-1;
        }
        if (start < 0) {
            System.out.println(" start < 0 ");
            fromIndex = 0;
        }

        toIndex = limit + start;
        if (toIndex >= size) {
            toIndex = size-1;
        }

        System.out.println("fromIndex: "+fromIndex+" , toIndex"+toIndex);

        return post.subList(fromIndex, toIndex);
    }

    public List<Post> sort(List<Post> sortedList, String sortField, String order) {
        if (sortField != null) {
            sortModel.setAuthor(false);
            sortModel.setExcerpt(false);
            sortModel.setPublishedAt(false);
            sortModel.setTitle(false);
            sortModel.setAsc(false);
            sortModel.setDesc(false);
            if (sortField.equals("author")) {
                sortModel.setAuthor(true);
                System.out.println("applying sort based on author");
                if (order != null && order.equals("desc")) {
                    sortModel.setDesc(true);
                    Collections.sort(sortedList, new PostAuthorComparatorDesc());
                } else {
                    sortModel.setAsc(true);
                    Collections.sort(sortedList, new PostAuthorComparatorAsc());
                }
            } else if (sortField.equals("title")) {
                sortModel.setTitle(true);
                System.out.println("applying sort based on title");
                if (order != null && order.equals("desc")) {
                    sortModel.setDesc(true);
                    Collections.sort(sortedList, new PostTitleComparatorDesc());
                } else {
                    sortModel.setAsc(true);
                    Collections.sort(sortedList, new PostTitleComparatorAsc());
                }
            } else if (sortField.equals("excerpt")) {
                sortModel.setExcerpt(true);
                System.out.println("applying sort based on excerpt");
                if (order != null && order.equals("desc")) {
                    sortModel.setDesc(true);
                    Collections.sort(sortedList, new PostExcerptComparatorDesc());
                } else {
                    sortModel.setAsc(true);
                    Collections.sort(sortedList, new PostExcerptComparatorAsc());
                }
            } else {
                sortModel.setPublishedAt(true);
                System.out.println("else : applying sort based on pubAt");
                if (order != null && order.equals("asc")) {
                    sortModel.setAsc(true);
                    Collections.sort(sortedList, new PostPublishedAtComparatorAsc());
                } else {
                    sortModel.setDesc(true);
                    Collections.sort(sortedList, new PostPublishedAtComparatorDesc());
                }
            }
        } else {
            sortModel.setAuthor(false);
            sortModel.setExcerpt(false);
            sortModel.setPublishedAt(true);
            sortModel.setTitle(false);
            sortModel.setAsc(false);
            sortModel.setDesc(false);

            System.out.println("sortField==null : applying sort based on sortField");
            if (order != null && order.equals("asc")) {
                sortModel.setAsc(true);
                Collections.sort(sortedList, new PostPublishedAtComparatorDesc());
            } else {
                Collections.sort(sortedList, new PostPublishedAtComparatorDesc());
            }
        }
        return sortedList;
    }

    final int LIMIT = 4;
    public List<PageModel> pagination(List<Post> posts, int start, int limit){
        int noOfRow = posts.size();
        List<PageModel> paginationUrl = new ArrayList<PageModel>();

        List<Integer> list = new ArrayList<>();

        int fromIndex=0, toIndex=LIMIT;
        if (start >= noOfRow) {
            fromIndex = noOfRow-1;
        }
        if (start < 0) {
            fromIndex = 0;
        }

        toIndex = limit + start;
        if (toIndex >= noOfRow) {
            toIndex = noOfRow-1;
        }

        if(noOfRow < LIMIT) {
            PageModel page1 = new PageModel();
            page1.setStart(0);
            page1.setLimit(noOfRow);
            page1.setPageNo(1);
            paginationUrl.add(page1);
        } else {
            int n = (noOfRow/LIMIT);
            int pageNo;
            int startIteration = 0;
            for(pageNo=1; pageNo<=n; pageNo++){
                PageModel page1 = new PageModel();//
                page1.setStart((pageNo*LIMIT)-LIMIT);//
                page1.setLimit((LIMIT));//
                page1.setPageNo(pageNo);//
                paginationUrl.add(page1);//
            }

            System.out.println("paginationUrl: "+paginationUrl.toString());

            int leftNoOfRow = noOfRow - (n*LIMIT);

            System.out.println("leftNoOfRow: "+leftNoOfRow+"  start:"+((pageNo*LIMIT)-LIMIT)+"  pageNo:"+pageNo);

            if(leftNoOfRow>0){
                PageModel page1 = new PageModel();
                page1.setStart((n*LIMIT)-LIMIT);
                page1.setLimit(leftNoOfRow);
                page1.setPageNo(pageNo);
                paginationUrl.add(page1);
            }
        }
        return paginationUrl;
    }

    public Set<Post> searchTag(String tag) {
        List<Tag> tags = tagRepository.findByTag(tag);
        Set<Post> tagSet = new LinkedHashSet<>();
        for (Tag eachTag : tags) {
            Optional<Post> optionalPost = postRepository.findById(eachTag.getPostId());
            if (!optionalPost.isEmpty()) {
                if (optionalPost.get() != null) tagSet.add(optionalPost.get());
            }
        }
        return tagSet;
    }

    PostService postService = new PostService();

    @PostMapping("post_updated")
    public String postUpdate(@ModelAttribute Post post) {
        new PostService().save(postRepository, post);
        return "redirect:/";
    }

    @GetMapping("/showBlogPost/deleteComment")
    public String deleteComment(@RequestParam(value = "id", required = false) int id) {
        Optional<Comment> commentResult = commentRepository.findById(id);
        commentRepository.deleteById(id);
        return "redirect:/showBlogPost?postId=" + commentResult.get().getPostId();
    }

    @GetMapping("/showBlogPost/updateComment")
    public String updateComment(@RequestParam("id") int commentId, Model model) {
        Optional<Comment> optional = commentRepository.findById(commentId);
        Comment comment = optional.get();
        model.addAttribute("comment", comment);
        return "comment/update_comment";
    }

    @PostMapping("showBlogPost/comment_updated")
    public String commentUpdated(@RequestParam(value = "id", required = false) Integer commentId, @RequestParam(value = "comment", required = false) String commentMsg) {
        Optional<Comment> optional = commentRepository.findById(commentId);
        Comment comment = optional.get();
        comment.setComment(commentMsg);
        comment.setId(commentId);
        int postId = comment.getPostId();
        Comment commentInserted = new CommentService().update(commentRepository, comment);
        return "redirect:/showBlogPost?postId=" + postId;
    }

    @GetMapping("/newpost")
    public String newpost() {
        return "post/newpost";
    }


    @PostMapping("/newpost_submitted")
    public String newpost_submit(@ModelAttribute Post posts, @RequestParam("tags") String tags) {
        Post postInserted = new PostService().save(postRepository, posts);
        new TagService().saveEachTag(tagRepository, tags, postInserted.getId(), postInserted.getCreated_at(), postInserted.getUpdated_at());
        return "redirect:/";
    }


    //################################################# for user

    @GetMapping("/register")
    public String index() {
        return "user/register";
    }

    @PostMapping("/register_done")
    public String register(@ModelAttribute User user, Model model) {
        User userDetails = new UserService().save(userRepository, user);
        model.addAttribute("message", userDetails.getFname() + " inserted.\n");
        return "welcome";
    }

    //################################################# for retrieve

    @GetMapping("/userslist")
    public String getUserList(Model model) {
        List<User> theUsers = new UserService().findAll(userRepository);
        model.addAttribute("users_list", theUsers);
        model.addAttribute("user5", userRepository.findById(5).toString());
        return "user/users_list";
    }

    @GetMapping("/showBlogPost")
    public String showBlogPost(@RequestParam("postId") int postId, Model postModel, Model tagModel, Model commentModel) {
        Post post = null;
        try {
            post = new PostService().findPostById(postRepository, postId);
        } catch (Exception e) {
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
    public String comments(@ModelAttribute("comments") Comment comment) {
        Comment comment_inserted = new CommentService().save(commentRepository, comment);
        return "redirect:/showBlogPost?postId=" + comment.getPostId();
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
