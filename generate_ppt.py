from pptx import Presentation
from pptx.util import Inches, Pt
from pptx.dml.color import RGBColor

prs = Presentation()
prs.slide_width = Inches(10)
prs.slide_height = Inches(5.625) # 16:9 widescreen

# Helper function to add slides
def add_slide(title_text, bullets):
    slide_layout = prs.slide_layouts[1] # Title and Content
    slide = prs.slides.add_slide(slide_layout)

    # Title
    title = slide.shapes.title
    title.text = title_text
    title.text_frame.paragraphs[0].font.size = Pt(28)
    title.text_frame.paragraphs[0].font.bold = True
    title.text_frame.paragraphs[0].font.color.rgb = RGBColor(0, 51, 102)

    # Content
    body = slide.placeholders[1]
    tf = body.text_frame
    tf.clear()

    for i, bullet in enumerate(bullets):
        p = tf.add_paragraph() if i > 0 else tf.paragraphs[0]
        p.text = bullet
        p.font.size = Pt(18)
        p.level = 0
        p.space_after = Pt(10)

# Slide 1: Title
slide_layout = prs.slide_layouts[0] # Title slide
slide = prs.slides.add_slide(slide_layout)
title = slide.shapes.title
subtitle = slide.placeholders[1]
title.text = "Health Appointment Management System"
subtitle.text = "Mobile Application Development Group Project (BBT 3.2)\nKotlin & Android Studio"

# Slide 2: Problem Statement
add_slide("Problem Statement", [
    "Traditional healthcare appointment systems rely on manual phone calls, physical booking desks, and paper records.",
    "• Scheduling Inefficiencies: Long wait times, double-bookings, and scheduling conflicts.",
    "• Lack of Accessibility: Patients struggle to find doctors by specialty or check availability outside clinic hours.",
    "• Disorganized Records: Paper prescriptions and medical histories are easily lost or hard to track.",
    "• Administrative Burden: Heavy workloads for clinic staff leading to human errors."
])

# Slide 3: Proposed Solution
add_slide("Proposed Solution", [
    "A centralized digital mobile application designed to streamline the entire healthcare booking lifecycle:",
    "• Digital Booking: Browse doctors by specialty, check schedules, and book consultations instantly.",
    "• Real-time Tracking: View upcoming, completed, and cancelled appointments using LiveData.",
    "• Secure Persistence: Local data storage powered by Android Room Database.",
    "• Digitized Records: Easy access to consultation history and prescriptions."
])

# Slide 4: App Architecture
add_slide("App Architecture (MVVM & Clean Layers)", [
    "• UI Layer (ui/): Activities, Fragments, and ViewModels handling user interactions.",
    "• Data Layer (data/): Entities, DAOs, Room Database, and Repository acting as the single source of truth.",
    "• Unidirectional Flow: Screens observe ViewModel LiveData -> ViewModel talks to Repository -> Repository queries Room Database.",
    "• Separation of Concerns: UI remains decoupled from database operations."
])

# Slide 5: Git & Team Collaboration
add_slide("Git Collaboration Workflow", [
    "• Branching Strategy: main (production), dev (integration), and feature/* for individual stages.",
    "• Pull Requests (PRs): Every teammate works on their branch, opens a PR, and gets code reviewed before merging.",
    "• Security: .gitignore properly configured to exclude local properties, build files, and signing keys (*.jks).",
    "• Collaborative Traceability: Every member contributes commits tracked via GitHub insights."
])

# Slide 6: App Distribution & Shipping
add_slide("App Distribution & Shipping (Stage 6)", [
    "• Signed APK: Generated production release build (.apk) signed securely with a keystore.",
    "• GitHub Releases: Published official v1.0 release on GitHub with downloadable APK asset.",
    "• Real-Device Testing: Fully tested on emulators and physical Android devices.",
    "• Comprehensive Documentation: Detailed README.md covering features, architecture, and team contributions."
])

# Slide 7: Team Roles & Contributions
add_slide("Team Roles & Contributions (5 Members)", [
    "• Stage 1 & 6 (Lead & Release): Repository setup, project structure, README, signed APK, and GitHub Release.",
    "• Stage 2 (ViewModel): Built ViewModel with LiveData and data transformations.",
    "• Stage 3 (Room DB): Implemented Room Database Entities, DAOs, and database configuration.",
    "• Stage 4 (Screens): Developed UI Fragments and navigation connected to ViewModels.",
    "• Stage 5 (Data Integration): Wired Repository and Room persistence into the ViewModel."
])

prs.save("Health_Appointment_Management_System_Presentation.pptx")
print("Presentation generated successfully!")
