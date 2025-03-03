export async function getWelcomePhrase() {
  const res = await fetch("/api/welcome-phrase");
  const data = await res.json();

  return data.phrase;
}
