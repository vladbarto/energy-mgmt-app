import { Component, EventEmitter, Input, Output } from '@angular/core';
import { Router } from "@angular/router";
import { UserModel } from "../../../../../../shared/models/user.model";

@Component({
  selector: 'app-user-card',
  templateUrl: './user-card.component.html',
  styleUrls: ['./user-card.component.css']
})
export class UserCardComponent {
  @Input() user!: UserModel;
  @Output() deleteUser: EventEmitter<string> = new EventEmitter<string>();
  @Output() saveUser: EventEmitter<UserModel> = new EventEmitter<UserModel>();

  editMode: boolean = false;
  editableUser!: UserModel;  // Temporary object for editable data

  constructor(private router: Router) {}

  viewDetails(userId: string): void {
    this.router.navigate(['/dashboard/user/' + userId]);
  }

  deleteUserPressed(userId: string | undefined): void {
    this.deleteUser.emit(userId);
  }

  editUser(): void {
    this.editMode = true;
    this.editableUser = { ...this.user }; // Make a copy of user data for editing
  }

  cancelEdit(): void {
    this.editMode = false;
  }

  save(): void {
    console.log('[user-card] se emite request de save');
    this.saveUser.emit(this.editableUser); // Emit the updated data to parent
    this.editMode = false; // Exit edit mode after saving
  }
}
