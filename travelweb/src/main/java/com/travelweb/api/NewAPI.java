package com.travelweb.api;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.travelweb.api.output.NewOutput;
import com.travelweb.dto.CategoryDTO;
import com.travelweb.dto.CommentDTO;
import com.travelweb.dto.NewDTO;
import com.travelweb.entity.CategoryEntity;
import com.travelweb.entity.CommentEntity;
import com.travelweb.entity.NewsEntity;
import com.travelweb.entity.UserEntity;
import com.travelweb.payload.response.MessageResponse;
import com.travelweb.repository.CategoryRepository;
import com.travelweb.repository.CommentRepository;
import com.travelweb.repository.NewRepository;
import com.travelweb.repository.UserRepository;
import com.travelweb.service.INewService;

import io.jsonwebtoken.io.IOException;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/news/")
public class NewAPI {
	@Autowired
	private INewService newService;
	
	@Autowired
	private Cloudinary cloudinary;
	
	@Autowired
    private CategoryRepository categoryRepository;
	
	@Autowired
    private NewRepository newsRepository;
	
	@Autowired
    private CommentRepository commentRepository;
	
	@Autowired
    private UserRepository userRepository;

	//phân trang
	@GetMapping(value = "/new")
	public NewOutput showNew(@RequestParam("page") int page,
							 @RequestParam("limit") int limit) {
		NewOutput result = new NewOutput();
		result.setPage(page);
		Pageable pageable = new PageRequest(page - 1, limit);
		result.setListResult(newService.findAll(pageable).reversed());
		result.setTotalPage((int) Math.ceil((double) (newService.totalItem()) / limit));
		return result;
	}
	
	@GetMapping(value = "/categories")
    public List<CategoryEntity> getAllCategories() {
	    return newService.getAllCategory();
    }
	
	@PostMapping(value = "/new", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public NewDTO createNew(@RequestParam("data") String data,
	                        @RequestParam("file") MultipartFile file) throws java.io.IOException {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	    System.out.println("Authentication: " + authentication);
	    System.out.println("Authenticated user: " + authentication.getName());
		
		try {
	    	
	    	System.out.println("Data: " + data);
	        System.out.println("File: " + file.getOriginalFilename());
	        
	        // Upload ảnh lên Cloudinary
	        Map<?, ?> uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
	        String imageUrl = uploadResult.get("secure_url").toString();
	        
//	        ObjectMapper mapper = new ObjectMapper();
//	        NewsEntity entity = mapper.readValue(data, NewsEntity.class);

	        // Chuyển đổi JSON thành đối tượng NewDTO
	        ObjectMapper objectMapper = new ObjectMapper();
	        NewDTO newDTO = objectMapper.readValue(data, NewDTO.class);

	        // Gán URL ảnh vào thuộc tính thumbnail
	        newDTO.setThumbnail(imageUrl);

	        // Lưu bài viết vào cơ sở dữ liệu
	        return newService.save(newDTO);
//	        createNew(newDTO);
	    } catch (IOException e) {
	        e.printStackTrace();
	        throw new RuntimeException("Lỗi khi tạo bài viết");
	    }
	}
	
	//Tìm kiếm theo tên
	@GetMapping("/search")
    public ResponseEntity<List<NewDTO>> searchNewsByTitle(@RequestParam("keyword") String keyword) {
		if (keyword == null || keyword.trim().isEmpty()) {
		    throw new IllegalArgumentException("Keyword is invalid");
		}
		
		System.out.println("Received keyword: " + keyword); // Log từ khóa nhận được
	    List<NewDTO> result = newService.searchByTitle(keyword.trim());
	    System.out.println("Received keyword to String: " + keyword.trim());
	    return ResponseEntity.ok(result);
	    
//        NewOutput result = new NewOutput();
//		result.setPage(1);
//		Pageable pageable = new PageRequest(1 - 1, 6);
//		result.setListResult(newService.searchByTitle(keyword));
//		result.setTotalPage((int) Math.ceil((double) (newService.totalItem()) / 6));
//        return result;
    }

	
	@PutMapping(value = "/new/{id}")
	public NewDTO updateNew(@RequestBody NewDTO model, @PathVariable("id") long id) {
		model.setId(id);
		return newService.save(model);
	}
	
//	@PutMapping(value = "/new/{name}")
//	public NewDTO updateNew(@RequestBody NewDTO model, @PathVariable("name") String name) {
//		model.setTitle(name);
//		return newService.save(model);
//	}
	
	@DeleteMapping(value = "/new")
	public void deleteNew(@RequestBody long[] ids) {
		newService.delete(ids);
	}
	
	@DeleteMapping("/new/{postId}")
	public ResponseEntity<?> deletePost(@PathVariable Long postId) {
	    newsRepository.deleteById(postId);
	    return ResponseEntity.ok(new MessageResponse("Bài viết đã bị xoá"));
	}

	
//	@PostMapping(value = "/new")
//	public NewOutput showNew(@RequestParam("page") int page,
//							 @RequestParam("limit") int limit) {
//		NewOutput result = new NewOutput();
//		result.setPage(page);
//		return result;
//	}

	//Lấy chi tiết bài viết theo ID
    @GetMapping("/new/{id}")
    public ResponseEntity<?> getPostById(@PathVariable Long id) {
    	Optional<NewsEntity> postOpt = newsRepository.findById(id);
        if (!postOpt.isPresent()) {
            return ResponseEntity.badRequest().body(new MessageResponse("Bài viết không tồn tại"));
        }

        NewsEntity post = postOpt.get();

        // Chuyển đổi từ Entity sang DTO
        NewDTO postDTO = new NewDTO();
        postDTO.setId(post.getId());
        postDTO.setTitle(post.getTitle());
        postDTO.setShortDescription(post.getShortDescription());
        postDTO.setTitleContent(post.getTitleContent());
        postDTO.setContent(post.getContent());
        postDTO.setThumbnail(post.getThumbnail());
        postDTO.setCreatedDate(post.getCreatedDates());
        postDTO.setCreatedBy(post.getCreatedBy());
        postDTO.setModifiedDate(post.getModifiedDates());
        postDTO.setModifiedBy(post.getModifiedBy());

        // Chuyển danh sách bình luận sang DTO
        List<CommentDTO> commentDTOs = post.getComments().stream().map(comment -> {
            CommentDTO dto = new CommentDTO();
            dto.setContent(comment.getContent());
            dto.setRating(comment.getRating());
            dto.setUsername(comment.getUser().getUsername());
            return dto;
        }).collect(Collectors.toList());

        postDTO.setComments(commentDTOs);

        return ResponseEntity.ok(postDTO);
    }

    //lấy bình luận theo ID bài viết
    @GetMapping("/new/{postId}/comments")
    public ResponseEntity<List<CommentDTO>> getCommentsByPost(@PathVariable Long postId) {
        Optional<NewsEntity> post = newsRepository.findById(postId);
        if (!post.isPresent()) {
            return ResponseEntity.badRequest().body(null);
        }

        List<CommentDTO> commentDTOs = commentRepository.findByPost(post.get()).stream().map(comment -> {
            CommentDTO dto = new CommentDTO();
            dto.setContent(comment.getContent());
            dto.setRating(comment.getRating());
            dto.setUsername(comment.getUser().getUsername());
            return dto;
        }).collect(Collectors.toList());

        return ResponseEntity.ok(commentDTOs);
    }

    //thêm bình luận mới vào bài viết
    @PostMapping("/new/{postId}/comments")
    public ResponseEntity<?> addComment(@PathVariable Long postId,
                                        @RequestParam Long userId,
                                        @RequestParam String content,
                                        @RequestParam int rating) {
    	System.out.println("Nhận dữ liệu: userId=" + userId + ", postId=" + postId + ", content=" + content + ", rating=" + rating);
        
    	if (userId == null || postId == null || content == null || content.trim().isEmpty()) {
            return ResponseEntity.badRequest().body(new MessageResponse("Dữ liệu bình luận không hợp lệ!"));
        }
    	
    	Optional<NewsEntity> postOpt = newsRepository.findById(postId);
        Optional<UserEntity> userOpt = userRepository.findById(userId);

        if (!postOpt.isPresent()) {
            return ResponseEntity.badRequest().body(new MessageResponse("Bài viết không tồn tại"));
        }
        if (!userOpt.isPresent()) {
            return ResponseEntity.badRequest().body(new MessageResponse("Người dùng không tồn tại"));
        }

        CommentEntity comment = new CommentEntity();
        comment.setPost(postOpt.get());
        comment.setUser(userOpt.get());
        comment.setContent(content);
        comment.setRating(rating);

        commentRepository.save(comment);
        return ResponseEntity.ok(new MessageResponse("Bình luận đã được thêm!"));
    }
}
