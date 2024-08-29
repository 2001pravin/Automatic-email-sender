import { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'
import App from './App.jsx'
import './index.css'
import EmailSender from './assets/EmailSender.jsx'

createRoot(document.getElementById('root')).render(
  <StrictMode>
   <EmailSender/>
  </StrictMode>,
)
