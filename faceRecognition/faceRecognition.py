import face_recognition
import sys

target_filename = sys.argv[1]
unknown_filename = sys.argv[2]

picture_of_target = face_recognition.load_image_file(target_filename)
known_face_encodings = face_recognition.face_encodings(picture_of_target)[0]

# my_face_encoding now contains a universal 'encoding' of my facial features that can be compared to any other picture of a face!

picture_of_unknown = face_recognition.load_image_file(unknown_filename)
face_locations = face_recognition.face_locations(picture_of_unknown)
face_encodings = face_recognition.face_encodings(picture_of_unknown, face_locations)

detected = False
for face_encoding in face_encodings:
    # See if the face is a match for the known face(s)
    result = face_recognition.compare_faces([known_face_encodings], face_encoding)
    if(result[0] == True):
        detected = True
        break
        
if(detected == True):
    print(1)
else:
    print(0)
