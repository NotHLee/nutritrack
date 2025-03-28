# NutriTrack App Checklist

## 3. Screens & Functionality

### 3.1. Welcome Screen (Landing Page)
- [x] Design app logo and name ("NutriTrack")
- [x] Add disclaimer text
- [x] Include external link to Monash Nutrition Clinic
- [x] Implement login button (navigates to login screen)

### 3.2. Login Screen
- [x] Create dropdown for User ID (loaded from CSV file)
- [x] Add text field for phone number
- [x] Implement continue button
- [x] Set up validation rules:
    - [x] Verify User ID exists in CSV
    - [x] Confirm phone number matches CSV data
    - [x] Display error message for invalid entries

### 3.3. Food Intake Questionnaire
- [x] Add checkboxes for food categories (Fruits, Vegetables, Grains, etc.)
- [x] Implement persona selection buttons (six categories)
- [x] Create dropdown for selecting best-fitting persona
- [x] Add time pickers for:
    - [x] Biggest meal time
    - [x] Sleep time
    - [x] Wake-up time
- [ ] Implement save button (stores data in SharedPreferences)

### 3.4. Home Screen (Food Quality Score)
- [ ] Add greeting: "Hello, [User's Name]"
- [ ] Implement edit button (returns to questionnaire)
- [ ] Display Food Score (loaded from CSV)
- [ ] Add explanation text about Food Quality Score
- [ ] Implement navigation options (bottom bar or buttons)

### 3.5. Insights Screen (Detailed Breakdown)
- [ ] Create progress bars for food categories:
    - [ ] Vegetables
    - [ ] Fruits
    - [ ] Grains & Cereals
    - [ ] Whole Grains
    - [ ] Meat
    - [ ] Dairy
- [ ] Display Total Score Calculation (from CSV)
- [ ] Add buttons:
    - [ ] "Share with someone"
    - [ ] "Improve my diet"

## 4. Data Handling & CSV Integration
- [x] Set up CSV file reading functionality
- [x] Implement User ID and Phone Number validation
- [ ] Configure Food Quality Score retrieval and display
- [ ] Add error handling for invalid entries

## 5. Requirements Assessment

### 5.1. Core Requirements
- [ ] Implement all five screens (Welcome, Login, Questionnaire, Home, Insights)
- [x] Configure CSV reading for User ID & Phone Number validation
- [ ] Display Food Quality Score from CSV
- [ ] Implement basic navigation

### 5.2. Not Required (Future Work)
- Settings screen
- NutriCoach AI recommendations

## 5b. Screen Mockup: Specifications
- [ ] Create low-fidelity wireframe for Screen 3 (Food Intake Questionnaire)
- [ ] Include all required functionality:
    - [ ] Food category selection
    - [ ] Persona selection
    - [ ] Meal timing inputs
- [ ] Label all UI elements clearly
- [ ] Write 50-100 word explanation of UX improvements
- [ ] Prepare submission:
    - [ ] Create PDF with mockup images (Page 1)
    - [ ] Add explanation (Page 2)
    - [ ] Name file: "Firstname_Lastname_Screen3Mockup.pdf"