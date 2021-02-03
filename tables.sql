CREATE TABLE USERS(
  userId serial PRIMARY KEY,
  fullName VARCHAR(100) NOT NULL,
  gender VARCHAR(10) NOT NULL,
  email VARCHAR(250) NOT NULL,
  userType VARCHAR(10) NOT NULL,   -- ADMIN or normal user
  mobile VARCHAR(20) NOT NULL,
  dob VARCHAR(20),
  password VARCHAR(250) NOT NULL,
  isActive BOOLEAN NOT NULL,
  created_on TIMESTAMP NOT NULL,
);

CREATE TABLE ADDRESS(
  addressId serial PRIMARY KEY,
  userId INTEGER,
  title VARCHAR(250),
  address1 VARCHAR(250),
  address2 VARCHAR(250),
  city VARCHAR(250) NOT NULL,
  pincode VARCHAR(20) NOT NULL,
  state VARCHAR(250) NOT NULL,
  country VARCHAR(250),
  mobile VARCHAR(250) NOT NULL,
  created_on TIMESTAMP NOT NULL,
  FOREIGN KEY (addressId) REFERENCES USERS(userId)
)


CREATE TABLE CART(
  cartId serial PRIMARY KEY,
  netTotal INTEGER NOT NULL,
  netQuantity INTEGER NOT NULL,
  productIds TEXT       -- Use @Lob in Modelling Entity
  productQuantities TEXT
  created_on TIMESTAMP NOT NULL,
  FOREIGN KEY (cartId) REFERENCES USERS(userId)
)

CREATE TABLE IMGMASTER(
  imgId serial PRIMARY KEY,
  imgPath VARCHAR(100),
  fileExtension VARCHAR(25) NOT NULL,
  imgData text NOT NULL,
  created_on TIMESTAMP NOT NULL,
)

CREATE TABLE BANNER(
  bannerId serial PRIMARY KEY,
  bannerName VARCHAR(250),
  imgPath INTEGER NOT NULL,
  created_on TIMESTAMP NOT NULL,
  FOREIGN KEY (imgPath) REFERENCES IMGMASTER(imgId)
)

CREATE TABLE CATEGORY(
  categoryId serial PRIMARY KEY,
  categoryName VARCHAR(250) NOT NULL,
  imgPath INTEGER NOT NULL,
  created_on TIMESTAMP NOT NULL,
  FOREIGN KEY (imgPath) REFERENCES IMGMASTER(imgId)
)

CREATE TABLE PRODUCT(
  productId serial PRIMARY KEY,
  productName VARCHAR(250) NOT NULL,
  brand VARCHAR(205) ,
  productDescription TEXT,
  imgPath INTEGER,
  categoryId INTEGER NOT NULL,
  oldPrice INTEGER,
  discountPercentage INTEGER,
  newPrice INTEGER,
  created_on TIMESTAMP NOT NULL,
  FOREIGN KEY (imgPath) REFERENCES IMGMASTER(imgId),
  FOREIGN KEY (categoryId) REFERENCES CATEGORY(categoryId)
)

CREATE TABLE COUPON(
  couponId serial PRIMARY KEY,
  couponName VARCHAR(50) NOT NULL,
  percentageDiscount INTEGER,
  maxDiscount INTEGER,
  created_on TIMESTAMP NOT NULL,
);

CREATE TABLE PINCODE(
  pincodeId serial PRIMARY KEY,
  isActive BOOLEAN NOT NULL,
  name VARCHAR(50) NOT NULL,
  created_on TIMESTAMP NOT NULL
);


CREATE TABLE ORDER(
  orderId serial PRIMARY KEY,
  userId INTEGER NOT NULL,
  addressId INTEGER NOT NULL,
  orderStatus VARCHAR(250) NOT NULL,
  finalTotal INTEGER NOT NULL,
  netQuantity INTEGER NOT NULL,
  total INTEGER NOT NULL,
  productIds TEXT       -- Use @Lob in Modelling Entity
  productQuantities TEXT
  discountApplied INTEGER,
  paymentMethod VARCHAR(250) NOT NULL,   -- ONLINE or COD
  onlinePaymentMode VARCHAR(250)    -- PhonePe or Google pay
  transactionId VARCHAR(250),
  cancellationReason VARCHAR(250),
  deliveryDate TIMESTAMP NOT NULL,
  created_on TIMESTAMP NOT NULL,
  safeDelivery BOOLEAN NOT NULL,
  FOREIGN KEY (userId) REFERENCES USERS(userId),
)

CREATE TABLE SESSIONS(
  sessionId serial PRIMARY KEY,
  userId INTEGER NOT NULL,
  token TEXT NOT NULL,
  created_on TIMESTAMP
)
