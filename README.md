# Chronicles 📅 ✍️

**Chronicles** is a local-first mobile journaling application designed to capture daily stories, hustles, and personal growth. Unlike traditional notes apps, Chronicles anchors every entry to a relational calendar and allows users to cluster their history into thematic "Chronicles."

---

## 🌟 The Core Concept
The problem with most journaling apps is that context gets lost over time. **Chronicles** solves this by introducing **Logical Grouping**. 

Users can group contiguous journals from a specific start date to an end date into a named folder called a **"Chronicle."**
* *Example:* Group entries from Feb 18th to March 20th as **"Ramadan Chronicles"** or a 6-week range as **"Internship Hustle."**

---

## 🚀 Features
- **Daily Journaling:** A clean interface to write and save daily ups and downs.
- **Calendar Integration:** A dynamic calendar view that acts as a relational anchor for all entries.
- **Fav-Marking:** Flag your most important journals for quick access.
- **Local Persistence:** High-speed, offline-first performance using an optimized SQLite database.
- **Chronicle Folders (Roadmap):** Grouping contiguous date ranges into custom thematic journeys.

---

## 🛠️ Tech Stack
- **Language:** Java / Android SDK
- **Database:** SQLite (Relational Schema Design)
- **UI:** XML with a focus on clean, intuitive navigation

---

## 🏗️ Database Logic
The app uses a relational schema to map entries to dates. Using **SQLite** allowed me to:
1. Maintain 100% user privacy (data never leaves the device).
2. Implement range-based queries using `BETWEEN` operators for the Chronicle grouping feature.
3. Handle efficient CRUD operations for high-performance daily logging.

---

## 📸 Preview
<img width="300"  alt="Image" src="https://github.com/user-attachments/assets/09d3dc45-1c0f-4d92-bf0d-602a2ec7a40a" />
<img width="300" alt="Image" src="https://github.com/user-attachments/assets/25597680-4bff-4ce3-bf4c-9be66aa66146" />
<img width="300"  alt="Image" src="https://github.com/user-attachments/assets/0141c7c9-1a8d-478a-a0c0-54159571feec" />
<img width="300"  alt="Image" src="https://github.com/user-attachments/assets/b15e0b22-cc00-4d9a-88d2-c04f42803dc3" />


---

## 📈 Roadmap
- [x] Firebase-free Local SQLite Integration
- [x] Calendar-linked Journaling
- [x] Favorite/Bookmark System
- [ ] **Next:** Implementation of the Chronicles Folder UI and Range-Query logic.

---

## 👨‍💻 About the Developer
I am a **Computer Engineering student** with a 4-year technical foundation (Diploma + Degree). I am a technical generalist passionate about building systems that bridge the gap between complex data and user-friendly interfaces.

---