package com.cus.jastip.profile.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

import com.cus.jastip.profile.domain.enumeration.ProfileStatus;

/**
 * A Profile.
 */
@Entity
@Table(name = "profile")
@Document(indexName = "profile")
public class Profile extends AbstractAuditingEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotNull
	@Size(min = 3)
	@Column(name = "name", nullable = false)
	private String name;

	@Enumerated(EnumType.STRING)
	@Column(name = "status")
	private ProfileStatus status;

	@Column(name = "active_date")
	private Instant activeDate;

	@Lob
	@Column(name = "image")
	private byte[] image;

	@Column(name = "image_content_type")
	private String imageContentType;

	@NotNull
	@Column(name = "email", nullable = false)
	private String email;

	@Column(name = "facebook_account")
	private String facebookAccount;

	@Column(name = "google_account")
	private String googleAccount;

	@Column(name = "phone_number")
	private String phoneNumber;

	@Column(name = "country_id")
	private Long countryId;

	@Column(name = "country_name")
	private String countryName;

	@Column(name = "city_id")
	private Long cityId;

	@Column(name = "city_name")
	private String cityName;

	@DecimalMin(value = "0")
	@Column(name = "average_rating")
	private Double averageRating;

	@Min(value = 0)
	@Column(name = "five_star_count")
	private Integer fiveStarCount;

	@Min(value = 0)
	@Column(name = "four_star_count")
	private Integer fourStarCount;

	@Min(value = 0)
	@Column(name = "three_star_count")
	private Integer threeStarCount;

	@Min(value = 0)
	@Column(name = "two_star_count")
	private Integer twoStarCount;

	@Min(value = 0)
	@Column(name = "one_star_count")
	private Integer oneStarCount;

	@Min(value = 0)
	@Column(name = "follower_count")
	private Integer followerCount;

	@Min(value = 0)
	@Column(name = "following_count")
	private Integer followingCount;

	@Min(value = 0)
	@Column(name = "request_count")
	private Integer requestCount;

	@Min(value = 0)
	@Column(name = "offers_count")
	private Integer offersCount;

	@Min(value = 0)
	@Column(name = "pre_order_count")
	private Integer preOrderCount;

	@Min(value = 0)
	@Column(name = "trip_count")
	private Integer tripCount;

	@Min(value = 0)
	@Column(name = "like_items_count")
	private Integer likeItemsCount;

	@NotNull
	@Column(name = "user_id", nullable = false)
	private Long userId;

	@NotNull
	@Column(name = "username", nullable = false)
	private String username;

	@Column(name = "url_image")
	private String urlImage;

	@Column(name = "url_image_thumb")
	private String urlImageThumb;

	@OneToMany(mappedBy = "profile")
	@JsonIgnore
	private Set<Address> addresses = new HashSet<>();

	@OneToMany(mappedBy = "profile")
	@JsonIgnore
	private Set<BankAccount> bankAccounts = new HashSet<>();

	@OneToMany(mappedBy = "profile")
	@JsonIgnore
	private Set<CreditCard> creditCards = new HashSet<>();

	@OneToMany(mappedBy = "profile")
	@JsonIgnore
	private Set<FollowingList> followingLists = new HashSet<>();

	@OneToMany(mappedBy = "profile")
	@JsonIgnore
	private Set<FollowerList> followerLists = new HashSet<>();

	@OneToMany(mappedBy = "profile")
	@JsonIgnore
	private Set<BlockList> blockLists = new HashSet<>();

	@OneToMany(mappedBy = "profile")
	@JsonIgnore
	private Set<BlockedByList> blockByLists = new HashSet<>();

	@OneToMany(mappedBy = "profile")
	@JsonIgnore
	private Set<Feedback> feedbacks = new HashSet<>();

	// jhipster-needle-entity-add-field - JHipster will add fields here, do not
	// remove
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public Profile name(String name) {
		this.name = name;
		return this;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ProfileStatus getStatus() {
		return status;
	}

	public Profile status(ProfileStatus status) {
		this.status = status;
		return this;
	}

	public void setStatus(ProfileStatus status) {
		this.status = status;
	}

	public Instant getActiveDate() {
		return activeDate;
	}

	public Profile activeDate(Instant activeDate) {
		this.activeDate = activeDate;
		return this;
	}

	public void setActiveDate(Instant activeDate) {
		this.activeDate = activeDate;
	}

	public byte[] getImage() {
		return image;
	}

	public Profile image(byte[] image) {
		this.image = image;
		return this;
	}

	public void setImage(byte[] image) {
		this.image = image;
	}

	public String getImageContentType() {
		return imageContentType;
	}

	public Profile imageContentType(String imageContentType) {
		this.imageContentType = imageContentType;
		return this;
	}

	public void setImageContentType(String imageContentType) {
		this.imageContentType = imageContentType;
	}

	public String getEmail() {
		return email;
	}

	public Profile email(String email) {
		this.email = email;
		return this;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getFacebookAccount() {
		return facebookAccount;
	}

	public Profile facebookAccount(String facebookAccount) {
		this.facebookAccount = facebookAccount;
		return this;
	}

	public void setFacebookAccount(String facebookAccount) {
		this.facebookAccount = facebookAccount;
	}

	public String getGoogleAccount() {
		return googleAccount;
	}

	public Profile googleAccount(String googleAccount) {
		this.googleAccount = googleAccount;
		return this;
	}

	public void setGoogleAccount(String googleAccount) {
		this.googleAccount = googleAccount;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public Profile phoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
		return this;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public Long getCountryId() {
		return countryId;
	}

	public Profile countryId(Long countryId) {
		this.countryId = countryId;
		return this;
	}

	public void setCountryId(Long countryId) {
		this.countryId = countryId;
	}

	public String getCountryName() {
		return countryName;
	}

	public Profile countryName(String countryName) {
		this.countryName = countryName;
		return this;
	}

	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}

	public Long getCityId() {
		return cityId;
	}

	public Profile cityId(Long cityId) {
		this.cityId = cityId;
		return this;
	}

	public void setCityId(Long cityId) {
		this.cityId = cityId;
	}

	public String getCityName() {
		return cityName;
	}

	public Profile cityName(String cityName) {
		this.cityName = cityName;
		return this;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public Double getAverageRating() {
		return averageRating;
	}

	public Profile averageRating(Double averageRating) {
		this.averageRating = averageRating;
		return this;
	}

	public void setAverageRating(Double averageRating) {
		this.averageRating = averageRating;
	}

	public Integer getFiveStarCount() {
		return fiveStarCount;
	}

	public Profile fiveStarCount(Integer fiveStarCount) {
		this.fiveStarCount = fiveStarCount;
		return this;
	}

	public void setFiveStarCount(Integer fiveStarCount) {
		this.fiveStarCount = fiveStarCount;
	}

	public Integer getFourStarCount() {
		return fourStarCount;
	}

	public Profile fourStarCount(Integer fourStarCount) {
		this.fourStarCount = fourStarCount;
		return this;
	}

	public void setFourStarCount(Integer fourStarCount) {
		this.fourStarCount = fourStarCount;
	}

	public Integer getThreeStarCount() {
		return threeStarCount;
	}

	public Profile threeStarCount(Integer threeStarCount) {
		this.threeStarCount = threeStarCount;
		return this;
	}

	public void setThreeStarCount(Integer threeStarCount) {
		this.threeStarCount = threeStarCount;
	}

	public Integer getTwoStarCount() {
		return twoStarCount;
	}

	public Profile twoStarCount(Integer twoStarCount) {
		this.twoStarCount = twoStarCount;
		return this;
	}

	public void setTwoStarCount(Integer twoStarCount) {
		this.twoStarCount = twoStarCount;
	}

	public Integer getOneStarCount() {
		return oneStarCount;
	}

	public Profile oneStarCount(Integer oneStarCount) {
		this.oneStarCount = oneStarCount;
		return this;
	}

	public void setOneStarCount(Integer oneStarCount) {
		this.oneStarCount = oneStarCount;
	}

	public Integer getFollowerCount() {
		return followerCount;
	}

	public Profile followerCount(Integer followerCount) {
		this.followerCount = followerCount;
		return this;
	}

	public void setFollowerCount(Integer followerCount) {
		this.followerCount = followerCount;
	}

	public Integer getFollowingCount() {
		return followingCount;
	}

	public Profile followingCount(Integer followingCount) {
		this.followingCount = followingCount;
		return this;
	}

	public void setFollowingCount(Integer followingCount) {
		this.followingCount = followingCount;
	}

	public Integer getRequestCount() {
		return requestCount;
	}

	public Profile requestCount(Integer requestCount) {
		this.requestCount = requestCount;
		return this;
	}

	public void setRequestCount(Integer requestCount) {
		this.requestCount = requestCount;
	}

	public Integer getOffersCount() {
		return offersCount;
	}

	public Profile offersCount(Integer offersCount) {
		this.offersCount = offersCount;
		return this;
	}

	public void setOffersCount(Integer offersCount) {
		this.offersCount = offersCount;
	}

	public Integer getPreOrderCount() {
		return preOrderCount;
	}

	public Profile preOrderCount(Integer preOrderCount) {
		this.preOrderCount = preOrderCount;
		return this;
	}

	public void setPreOrderCount(Integer preOrderCount) {
		this.preOrderCount = preOrderCount;
	}

	public Integer getTripCount() {
		return tripCount;
	}

	public Profile tripCount(Integer tripCount) {
		this.tripCount = tripCount;
		return this;
	}

	public void setTripCount(Integer tripCount) {
		this.tripCount = tripCount;
	}

	public Integer getLikeItemsCount() {
		return likeItemsCount;
	}

	public Profile likeItemsCount(Integer likeItemsCount) {
		this.likeItemsCount = likeItemsCount;
		return this;
	}

	public void setLikeItemsCount(Integer likeItemsCount) {
		this.likeItemsCount = likeItemsCount;
	}

	public Long getUserId() {
		return userId;
	}

	public Profile userId(Long userId) {
		this.userId = userId;
		return this;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getUsername() {
		return username;
	}

	public Profile username(String username) {
		this.username = username;
		return this;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getUrlImage() {
		return urlImage;
	}

	public Profile urlImage(String urlImage) {
		this.urlImage = urlImage;
		return this;
	}

	public void setUrlImage(String urlImage) {
		this.urlImage = urlImage;
	}

	public String getUrlImageThumb() {
		return urlImageThumb;
	}

	public Profile urlImageThumb(String urlImageThumb) {
		this.urlImageThumb = urlImageThumb;
		return this;
	}

	public void setUrlImageThumb(String urlImageThumb) {
		this.urlImageThumb = urlImageThumb;
	}

	public Set<Address> getAddresses() {
		return addresses;
	}

	public Profile addresses(Set<Address> addresses) {
		this.addresses = addresses;
		return this;
	}

	public Profile addAddress(Address address) {
		this.addresses.add(address);
		address.setProfile(this);
		return this;
	}

	public Profile removeAddress(Address address) {
		this.addresses.remove(address);
		address.setProfile(null);
		return this;
	}

	public void setAddresses(Set<Address> addresses) {
		this.addresses = addresses;
	}

	public Set<BankAccount> getBankAccounts() {
		return bankAccounts;
	}

	public Profile bankAccounts(Set<BankAccount> bankAccounts) {
		this.bankAccounts = bankAccounts;
		return this;
	}

	public Profile addBankAccount(BankAccount bankAccount) {
		this.bankAccounts.add(bankAccount);
		bankAccount.setProfile(this);
		return this;
	}

	public Profile removeBankAccount(BankAccount bankAccount) {
		this.bankAccounts.remove(bankAccount);
		bankAccount.setProfile(null);
		return this;
	}

	public void setBankAccounts(Set<BankAccount> bankAccounts) {
		this.bankAccounts = bankAccounts;
	}

	public Set<CreditCard> getCreditCards() {
		return creditCards;
	}

	public Profile creditCards(Set<CreditCard> creditCards) {
		this.creditCards = creditCards;
		return this;
	}

	public Profile addCreditCard(CreditCard creditCard) {
		this.creditCards.add(creditCard);
		creditCard.setProfile(this);
		return this;
	}

	public Profile removeCreditCard(CreditCard creditCard) {
		this.creditCards.remove(creditCard);
		creditCard.setProfile(null);
		return this;
	}

	public void setCreditCards(Set<CreditCard> creditCards) {
		this.creditCards = creditCards;
	}

	public Set<FollowingList> getFollowingLists() {
		return followingLists;
	}

	public Profile followingLists(Set<FollowingList> followingLists) {
		this.followingLists = followingLists;
		return this;
	}

	public Profile addFollowingList(FollowingList followingList) {
		this.followingLists.add(followingList);
		followingList.setProfile(this);
		return this;
	}

	public Profile removeFollowingList(FollowingList followingList) {
		this.followingLists.remove(followingList);
		followingList.setProfile(null);
		return this;
	}

	public void setFollowingLists(Set<FollowingList> followingLists) {
		this.followingLists = followingLists;
	}

	public Set<FollowerList> getFollowerLists() {
		return followerLists;
	}

	public Profile followerLists(Set<FollowerList> followerLists) {
		this.followerLists = followerLists;
		return this;
	}

	public Profile addFollowerList(FollowerList followerList) {
		this.followerLists.add(followerList);
		followerList.setProfile(this);
		return this;
	}

	public Profile removeFollowerList(FollowerList followerList) {
		this.followerLists.remove(followerList);
		followerList.setProfile(null);
		return this;
	}

	public void setFollowerLists(Set<FollowerList> followerLists) {
		this.followerLists = followerLists;
	}

	public Set<BlockList> getBlockLists() {
		return blockLists;
	}

	public Profile blockLists(Set<BlockList> blockLists) {
		this.blockLists = blockLists;
		return this;
	}

	public Profile addBlockList(BlockList blockList) {
		this.blockLists.add(blockList);
		blockList.setProfile(this);
		return this;
	}

	public Profile removeBlockList(BlockList blockList) {
		this.blockLists.remove(blockList);
		blockList.setProfile(null);
		return this;
	}

	public void setBlockLists(Set<BlockList> blockLists) {
		this.blockLists = blockLists;
	}

	public Set<BlockedByList> getBlockByLists() {
		return blockByLists;
	}

	public Profile blockByLists(Set<BlockedByList> blockedByLists) {
		this.blockByLists = blockedByLists;
		return this;
	}

	public Profile addBlockByList(BlockedByList blockedByList) {
		this.blockByLists.add(blockedByList);
		blockedByList.setProfile(this);
		return this;
	}

	public Profile removeBlockByList(BlockedByList blockedByList) {
		this.blockByLists.remove(blockedByList);
		blockedByList.setProfile(null);
		return this;
	}

	public void setBlockByLists(Set<BlockedByList> blockedByLists) {
		this.blockByLists = blockedByLists;
	}

	public Set<Feedback> getFeedbacks() {
		return feedbacks;
	}

	public Profile feedbacks(Set<Feedback> feedbacks) {
		this.feedbacks = feedbacks;
		return this;
	}

	public Profile addFeedback(Feedback feedback) {
		this.feedbacks.add(feedback);
		feedback.setProfile(this);
		return this;
	}

	public Profile removeFeedback(Feedback feedback) {
		this.feedbacks.remove(feedback);
		feedback.setProfile(null);
		return this;
	}

	public void setFeedbacks(Set<Feedback> feedbacks) {
		this.feedbacks = feedbacks;
	}
	// jhipster-needle-entity-add-getters-setters - JHipster will add getters and
	// setters here, do not remove

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		Profile profile = (Profile) o;
		if (profile.getId() == null || getId() == null) {
			return false;
		}
		return Objects.equals(getId(), profile.getId());
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(getId());
	}

	@Override
	public String toString() {
		return "Profile{" + "id=" + getId() + ", name='" + getName() + "'" + ", status='" + getStatus() + "'"
				+ ", activeDate='" + getActiveDate() + "'" + ", image='" + getImage() + "'" + ", imageContentType='"
				+ getImageContentType() + "'" + ", email='" + getEmail() + "'" + ", facebookAccount='"
				+ getFacebookAccount() + "'" + ", googleAccount='" + getGoogleAccount() + "'" + ", phoneNumber='"
				+ getPhoneNumber() + "'" + ", countryId=" + getCountryId() + ", countryName='" + getCountryName() + "'"
				+ ", cityId=" + getCityId() + ", cityName='" + getCityName() + "'" + ", averageRating="
				+ getAverageRating() + ", fiveStarCount=" + getFiveStarCount() + ", fourStarCount=" + getFourStarCount()
				+ ", threeStarCount=" + getThreeStarCount() + ", twoStarCount=" + getTwoStarCount() + ", oneStarCount="
				+ getOneStarCount() + ", followerCount=" + getFollowerCount() + ", followingCount="
				+ getFollowingCount() + ", requestCount=" + getRequestCount() + ", offersCount=" + getOffersCount()
				+ ", preOrderCount=" + getPreOrderCount() + ", tripCount=" + getTripCount() + ", likeItemsCount="
				+ getLikeItemsCount() + ", userId=" + getUserId() + ", username='" + getUsername() + "'"
				+ ", urlImage='" + getUrlImage() + "'" + ", urlImageThumb='" + getUrlImageThumb() + "'" + "}";
	}
}
