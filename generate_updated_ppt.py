from pptx import Presentation
from pptx.util import Inches, Pt
from pptx.dml.color import RGBColor

prs = Presentation()
prs.slide_width = Inches(10)
prs.slide_height = Inches(5.625) # 16:9 widescreen

def add_slide(title_text, bullets):
    slide_layout = prs.slide_layouts[1]
    slide = prs.slides.add_slide(slide_layout)

    title = slide.shapes.title
    title.text = title_text
    title.text_frame.paragraphs[0].font.size = Pt(26)
    title.text_frame.paragraphs[0].font.bold = True
    title.text_frame.paragraphs[0].font.color.rgb = RGBColor(0, 51, 102)

    body = slide.placeholders[1]
    tf = body.text_frame
    tf.clear()

    for i, bullet in enumerate(bullets):
        p = tf.add_paragraph() if i > 0 else tf.paragraphs[0]
        p.text = bullet
        p.font.size = Pt(16)
        p.level = 0
        p.space_after = Pt(8)

# Slide 1: Title
slide_layout = prs.slide_layouts[0]
slide = prs.slides.add_slide(slide_layout)
title = slide.shapes.title
subtitle = slide.placeholders[1]
title.text = "Health Appointment Management System"
subtitle.text = "Mobile Application Development Group Project (BBT 3.2)\nComplete Android Solution in Kotlin"

# Slide 2: Problem & Solution
add_slide("Problem Statement & Solution", [
    "• Problem: Traditional healthcare scheduling relies on manual phone calls, long wait times, scheduling conflicts, and lost paper medical records.",
    "• Solution: A centralized digital Health Appointment Management System bridging patients, doctors, and clinic administrators in real-time.",
    "• Key Pillars: Seamless digital booking, persistent patient records, secure role-based access, and automated clinical workflows."
])

# Slide 3: Authentication & Security
add_slide("Authentication & Security", [
    "• Role-Based Access: Dedicated portals for Patients, Doctors, and Administrators.",
    "• Secure Registration & Login: Password show/hide visibility toggle and email validation.",
    "• Password Reset: Interactive forgot-password link simulation for patients and doctors.",
    "• Doctor Profiles: Registration includes hospital/clinic assignment and clinical specialty (e.g. Back Doctor, Eye, Cardiologist)."
])

# Slide 4: Patient Portal Features
add_slide("Patient Portal Capabilities", [
    "• Appointment Booking: Choose registered doctors by specialty, date, time, and specify symptoms / reason for visit.",
    "• Digital Intake Forms: Pre-visit health questionnaires (symptoms, allergies, medications) saved to the database.",
    "• Prescriptions & Refills: View active prescriptions and send refill requests directly to doctors.",
    "• Additional Tools: Consultation billing & copay payments, care team messaging, and family/dependent profiles."
])

# Slide 5: Doctor Portal & Clinical Tools
add_slide("Doctor Portal & Clinical Tools", [
    "• Schedule & Shifts: Manage working hours and block time off or vacations.",
    "• Waiting Room: Real-time patient check-ins and appointment status tracking.",
    "• Patient Medical History: Review patient profiles and submitted pre-visit intake questionnaires.",
    "• E-Prescriptions & Lab Orders: Dynamic patient dropdown selection for targeted e-prescriptions and refill requests."
])

# Slide 6: Admin Dashboard
add_slide("Admin Dashboard & Management", [
    "• Centralized Oversight: Real-time overview of total doctors, patients, and system appointments.",
    "• User Administration: Add or remove doctors and patients securely.",
    "• Live Search: Instant search across all registered doctors and patients.",
    "• Appointment Control: Cancel or delete any clinic appointment directly from the admin panel."
])

# Slide 7: Architecture & Git Workflow
add_slide("Architecture & Git Collaboration", [
    "• Architecture: MVVM (Model-View-ViewModel) pattern ensuring clean separation between UI screens and data repositories.",
    "• Data Persistence: Local SQLite database operations supporting offline-first reliability.",
    "• Git Workflow: Feature branching (feature/*), pull requests, and peer reviews managed by the team lead."
])

# Slide 8: Future Enhancements
add_slide("Future Enhancements & Scaling", [
    "• M-Pesa Mobile Money: Safaricom M-Pesa STK push integration for instant copay and bill payments.",
    "• Push Notifications: Firebase Cloud Messaging (FCM) for real-time visit reminders.",
    "• Telehealth: Built-in WebRTC video calling for virtual consultations.",
    "• Automated SMS: Twilio / Africa's Talking integration for appointment reminder texts."
])

# Slide 9: Team Contributions
add_slide("Team Contributions (5 Members)", [
    "• Stage 1 & 6 (Lead & Release): Aman Singh Bhamra — Repository setup, architecture, README, signed APK v1.0.",
    "• Stage 2 (ViewModel): Moses Bill Odhiambo — ViewModel & LiveData management.",
    "• Stage 3 (Room DB): Ruweidha Mohamed Somo — Database entities and DAO architecture.",
    "• Stage 4 (Screens): Lee Mutua Wambua — User interface fragments and navigation.",
    "• Stage 5 (Data Integration): Alia Baba Grace — Repository persistence and data binding."
])

prs.save("Health_Appointment_Management_System_Presentation.pptx")
print("Updated presentation generated successfully!")
