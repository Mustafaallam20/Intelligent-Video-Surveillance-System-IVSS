import bark

# Load the min model
model = bark.load_model("min")

# Generate audio from the text prompt
text_prompt = """
     Hello, my name is Suno. And, uh — and I like pizza. [laughs] 
     But I also have other interests such as playing tic tac toe.
"""
audio_array = generate_audio(text_prompt)

# Save audio to disk
write_wav("bark_generation.wav", SAMPLE_RATE, audio_array)

# Play text in notebook
Audio(audio_array, rate=SAMPLE_RATE)
